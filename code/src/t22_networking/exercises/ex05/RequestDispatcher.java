package t22_networking.exercises.ex05;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Exercise 04's dispatcher plus DISCONNECT and stricter SUBMIT_SCORE validation.
 * Still no socket code.
 */
public class RequestDispatcher {

    public static final int MAX_SCORE = 999_999;

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
        handlers.put(RequestType.DISCONNECT, req -> ServerResponse.ok("goodbye", null));
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
        int score = req.getInt("score");
        if (score > MAX_SCORE)
            throw new IllegalArgumentException("score exceeds maximum of " + MAX_SCORE);

        String modeText = req.getString("gameMode");
        GameMode mode = GameMode.parse(modeText)
            .orElseThrow(() -> new IllegalArgumentException("invalid game mode: " + modeText));

        // ScoreEntry's constructor still rejects a missing playerId or playerName
        ScoreEntry inserted = repo.insert(
            req.getString("playerId"), req.getString("playerName"), score, mode.name());
        return ServerResponse.ok("score recorded", inserted);
    }

    private ServerResponse<?> getTopN(ClientRequest req) {
        int n = req.getInt("n");
        if (n <= 0)
            throw new IllegalArgumentException("n must be >= 1");
        return ServerResponse.ok("top " + n, repo.getTop(n));
    }
}
