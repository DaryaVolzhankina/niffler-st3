package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> usersWithFriends = new ConcurrentLinkedQueue<>();
        usersWithFriends.add(bindUser("dima", "12345"));
        usersWithFriends.add(bindUser("barsik", "12345"));
        usersQueue.put(User.UserType.WITH_FRIENDS, usersWithFriends);
        Queue<UserJson> usersInSent = new ConcurrentLinkedQueue<>();
        usersInSent.add(bindUser("bee", "12345"));
        usersInSent.add(bindUser("anna", "12345"));
        usersQueue.put(User.UserType.INVITATION_SENT, usersInSent);
        Queue<UserJson> usersInRc = new ConcurrentLinkedQueue<>();
        usersInRc.add(bindUser("valentin", "12345"));
        usersInRc.add(bindUser("pizzly", "12345"));
        usersQueue.put(User.UserType.INVITATION_RECEIVED, usersInRc);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Optional<Method> beforeEachMethod = Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class)).findFirst();
        Parameter[] parameters1 = beforeEachMethod.get().getParameters();
        Parameter[] parameters2 = context.getRequiredTestMethod().getParameters();
        Parameter[] parameters = Stream.concat(Arrays.stream(parameters1), Arrays.stream(parameters2)).toArray(
                size -> (Parameter[]) Array.newInstance(parameters1.getClass().getComponentType(), size));
        Map<User.UserType, UserJson> candidatesForTest = new HashMap<>();
        for (Parameter parameter : parameters) {
            if (parameter.getType().isAssignableFrom(UserJson.class) && parameter.isAnnotationPresent(User.class)) {
                User parameterAnnotation = parameter.getAnnotation(User.class);
                User.UserType userType = parameterAnnotation.userType();
                Queue<UserJson> usersQueueByType = usersQueue.get(parameterAnnotation.userType());
                UserJson candidateForTest = null;
                while (candidateForTest == null) {
                    candidateForTest = usersQueueByType.poll();
                }
                candidateForTest.setUserType(userType);
                candidatesForTest.put(userType, candidateForTest);
            }
        }
        context.getStore(NAMESPACE).put(getAllureId(context), candidatesForTest);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE).get(getAllureId(context), Map.class);
        for (User.UserType userType : usersFromTest.keySet()) usersQueue.get(userType).add(usersFromTest.get(userType));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).userType();
        return (UserJson) extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext), Map.class).get(userType);
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation @AllureId must be present!");
        }
        return allureId.value();
    }

    private static UserJson bindUser(String username, String password) {
        UserJson user = new UserJson();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
