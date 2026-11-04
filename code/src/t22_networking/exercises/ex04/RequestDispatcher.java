package t22_networking.exercises.ex04;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Routes each request type to a handler. Contains no socket code, so it can be
 * tested with plain method calls.
 */
public class RequestDispatcher {

    // === Fields ===
    private final ScoreRepository repo;
    private final Map<RequestType, RequestHandler> handlers = new EnumMap<>(RequestType.class);

    // === Constructors ===
    public RequestDispatcher(ScoreRepository repo) {
        if (repo == null)
            throw new IllegalArgumentException("repo is required");
        this.repo = repo;

        handlers.put(RequestType.GET_ALL, this::getAll);
        handlers.put(RequestType.SUBMIT_SCORE, this::submitScore);
        handlers.put(RequestType.GET_TOP_N, this::getTopN);
    }

    // === Public API ===
    // Dispatches: one parsed request; never throws
    public ServerResponse<?> dispatch(ClientRequest request) {
        if (request == null || request.getRequestType() == null)
            return ServerResponse.error("request has no requestType");

        RequestHandler handler = handlers.get(request.getRequestType());
        if (handler == null)
            return ServerResponse.error("unsupported request type: " + request.getRequestType());

        try {
            return handler.handle(request);
        }
        catch (IllegalArgumentException e) {
            return ServerResponse.error("invalid request: " + e.getMessage());
        }
    }

    // === Handlers ===
    private ServerResponse<?> getAll(ClientRequest req) {
        List<ScoreEntry> all = repo.getAll();
        return ServerResponse.ok(all.size() + " scores", all);
    }

    private ServerResponse<?> submitScore(ClientRequest req) {
        // ScoreEntry's constructor rejects missing fields and negative scores
        ScoreEntry inserted = repo.insert(
            req.getString("playerId"),
            req.getString("playerName"),
            req.getInt("score"),
            req.getString("gameMode"));
        return ServerResponse.ok("score recorded", inserted);
    }

    private ServerResponse<?> getTopN(ClientRequest req) {
        int n = req.getInt("n");
        if (n <= 0)
            throw new IllegalArgumentException("n must be >= 1");
        return ServerResponse.ok("top " + n, repo.getTop(n));
    }
}
