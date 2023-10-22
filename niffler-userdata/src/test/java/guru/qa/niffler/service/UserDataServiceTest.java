package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendsEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static guru.qa.niffler.model.FriendState.FRIEND;
import static guru.qa.niffler.model.FriendState.INVITE_RECEIVED;
import static guru.qa.niffler.model.FriendState.INVITE_SENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {
    private UserDataService testedObject;

    private UUID firstTestUserUuid = UUID.randomUUID();
    private String firstTestUserName = "dima";
    private UserEntity firstTestUser;

    private UUID secondTestUserUuid = UUID.randomUUID();
    private String secondTestUserName = "barsik";
    private UserEntity secondTestUser;

    private UUID thirdTestUserUuid = UUID.randomUUID();
    private String thirdTestUserName = "daria";
    private UserEntity thirdTestUser;


    private String notExistingUser = "not_existing_user";

    @BeforeEach
    void init() {
        firstTestUser = new UserEntity();
        firstTestUser.setId(firstTestUserUuid);
        firstTestUser.setUsername(firstTestUserName);
        firstTestUser.setCurrency(CurrencyValues.RUB);

        secondTestUser = new UserEntity();
        secondTestUser.setId(secondTestUserUuid);
        secondTestUser.setUsername(secondTestUserName);
        secondTestUser.setCurrency(CurrencyValues.RUB);

        thirdTestUser = new UserEntity();
        thirdTestUser.setId(thirdTestUserUuid);
        thirdTestUser.setUsername(thirdTestUserName);
        thirdTestUser.setCurrency(CurrencyValues.RUB);
    }

    @ValueSource(strings = {"photo", ""})
    @ParameterizedTest
    void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(firstTestUserName)))
                .thenReturn(firstTestUser);

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        testedObject = new UserDataService(userRepository);

        final String photoForTest = photo.equals("") ? null : photo;

        final UserJson toBeUpdated = new UserJson();
        toBeUpdated.setUsername(firstTestUserName);
        toBeUpdated.setFirstname("Test");
        toBeUpdated.setSurname("TestSurname");
        toBeUpdated.setCurrency(CurrencyValues.USD);
        toBeUpdated.setPhoto(photoForTest);
        final UserJson result = testedObject.update(toBeUpdated);
        assertEquals(firstTestUserUuid, result.getId());
        assertEquals("Test", result.getFirstname());
        assertEquals("TestSurname", result.getSurname());
        assertEquals(CurrencyValues.USD, result.getCurrency());
        assertEquals(photoForTest, result.getPhoto());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getRequiredUserShouldThrowNotFoundExceptionIfUserNotFound(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(notExistingUser)))
                .thenReturn(null);

        testedObject = new UserDataService(userRepository);

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> testedObject.getRequiredUser(notExistingUser));
        assertEquals(
                "Can`t find user by username: " + notExistingUser,
                exception.getMessage()
        );
    }

    @Test
    void allUsersShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {
        when(userRepository.findByUsernameNot(eq(firstTestUserName)))
                .thenReturn(getMockUsersMappingFromDb());

        testedObject = new UserDataService(userRepository);

        List<UserJson> users = testedObject.allUsers(firstTestUserName);
        assertEquals(2, users.size());
        final UserJson invitation = users.stream()
                .filter(u -> u.getFriendState() == INVITE_SENT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Friend with state INVITE_SENT not found"));

        final UserJson friend = users.stream()
                .filter(u -> u.getFriendState() == FRIEND)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Friend with state FRIEND not found"));


        assertEquals(secondTestUserName, invitation.getUsername());
        assertEquals(thirdTestUserName, friend.getUsername());
    }


    static Stream<Arguments> friendsShouldReturnDifferentListsBasedOnIncludePendingParam() {
        return Stream.of(
                Arguments.of(true, List.of(INVITE_SENT, FRIEND)),
                Arguments.of(false, List.of(FRIEND))
        );
    }

    @MethodSource
    @ParameterizedTest
    void friendsShouldReturnDifferentListsBasedOnIncludePendingParam(boolean includePending,
                                                                     List<FriendState> expectedStates,
                                                                     @Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(firstTestUserName)))
                .thenReturn(enrichFirstTestUser());

        testedObject = new UserDataService(userRepository);
        final List<UserJson> result = testedObject.friends(firstTestUserName, includePending);
        assertEquals(expectedStates.size(), result.size());

        assertTrue(result.stream()
                .map(UserJson::getFriendState)
                .collect(Collectors.toList())
                .containsAll(expectedStates));
    }

    @Test
    void invitationsShouldReturnInviteReceivedList(@Mock UserRepository userRepository) {

        when(userRepository.findByUsername(eq(thirdTestUserName)))
                .thenReturn(enrichThirdTestUser());
        testedObject = new UserDataService(userRepository);
        final List<UserJson> result = testedObject.invitations(thirdTestUserName);
        assertTrue(result.stream()
                .map(UserJson::getFriendState)
                .collect(Collectors.toList())
                .contains(INVITE_RECEIVED));
    }

    @Test
    void invitationShouldBeAccepted(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(thirdTestUserName)))
                .thenReturn(enrichThirdTestUser());

        when(userRepository.findByUsername(eq(firstTestUserName)))
                .thenReturn(firstTestUser);
        testedObject = new UserDataService(userRepository);
        FriendJson friendJson = new FriendJson();
        friendJson.setUsername(firstTestUserName);
        final List<UserJson> result = testedObject.acceptInvitation(thirdTestUserName, friendJson);

        assertEquals(FRIEND, result.get(0).getFriendState());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void invitationShouldBeDeclined(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(thirdTestUserName)))
                .thenReturn(enrichThirdTestUser());
        when(userRepository.findByUsername(eq(firstTestUserName)))
                .thenReturn(firstTestUser);

        testedObject = new UserDataService(userRepository);
        FriendJson friendJson = new FriendJson();
        friendJson.setUsername(firstTestUserName);
        final List<UserJson> result = testedObject.declineInvitation(thirdTestUserName, friendJson);

        assertEquals(INVITE_RECEIVED, result.get(0).getFriendState());
        verify(userRepository, times(2)).save(any(UserEntity.class));
    }

    @Test
    void friendShouldBeRemoved(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(firstTestUserName)))
                .thenReturn(enrichFirstTestUser());
        when(userRepository.findByUsername(eq(secondTestUserName)))
                .thenReturn(secondTestUser);

        testedObject = new UserDataService(userRepository);
        final List<UserJson> result = testedObject.removeFriend(firstTestUserName, secondTestUserName);
        List<UserJson> friends = result
                .stream()
                .filter(friend -> friend.getUsername().equals(secondTestUserName))
                .collect(Collectors.toList());
        List<FriendsEntity> invites = firstTestUser.getInvites()
                .stream()
                .filter(invite -> invite.getFriend().getUsername().equals(secondTestUserName))
                .collect(Collectors.toList());

        assertEquals(0, friends.size());
        assertEquals(0, invites.size());
        verify(userRepository, times(2)).save(any(UserEntity.class));
    }


    private UserEntity enrichFirstTestUser() {
        firstTestUser.addFriends(true, thirdTestUser);
        thirdTestUser.addInvites(firstTestUser);

        firstTestUser.addFriends(false, secondTestUser);
        secondTestUser.addFriends(false, firstTestUser);
        return firstTestUser;
    }


    private UserEntity enrichThirdTestUser() {
        firstTestUser.addFriends(true, thirdTestUser);
        thirdTestUser.addInvites(firstTestUser);

        secondTestUser.addFriends(false, thirdTestUser);
        thirdTestUser.addInvites(secondTestUser);
        return thirdTestUser;
    }

    private List<UserEntity> getMockUsersMappingFromDb() {
        firstTestUser.addFriends(true, secondTestUser);
        secondTestUser.addInvites(firstTestUser);

        firstTestUser.addFriends(false, thirdTestUser);
        thirdTestUser.addFriends(false, firstTestUser);

        return List.of(secondTestUser, thirdTestUser);
    }
}