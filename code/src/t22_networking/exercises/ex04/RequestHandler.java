package t22_networking.exercises.ex04;

// One handler per request type
@FunctionalInterface
public interface RequestHandler {
    ServerResponse<?> handle(ClientRequest request);
}
