package t20_json_1_jackson_basics.exercises.ex03;

public class GameAsset {

    // === Fields ===
    private int    _assetId;
    private String _assetName;
    private String _assetType;
    private int    _fileSize;
    private byte[] _assetData;

    // === Constructors ===
    // Creates: a validated game asset
    public GameAsset(int assetId, String assetName, String assetType, int fileSize, byte[] assetData) {
        if (assetId < 0)
            throw new IllegalArgumentException("assetId must be >= 0");
        if (assetName == null || assetName.isBlank())
            throw new IllegalArgumentException("assetName is required");
        if (assetType == null || assetType.isBlank())
            throw new IllegalArgumentException("assetType is required");
        if (fileSize < 0)
            throw new IllegalArgumentException("fileSize must be >= 0");
        if (assetData == null || assetData.length == 0)
            throw new IllegalArgumentException("assetData is required");

        _assetId   = assetId;
        _assetName = assetName.trim();
        _assetType = assetType.trim().toLowerCase();
        _fileSize  = fileSize;
        _assetData = assetData;
    }

    // === Public API ===
    public int    getAssetId()   { return _assetId; }
    public String getAssetName() { return _assetName; }
    public String getAssetType() { return _assetType; }
    public int    getFileSize()  { return _fileSize; }
    public byte[] getAssetData() { return _assetData; }
}
