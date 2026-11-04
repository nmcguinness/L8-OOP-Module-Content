package t21_json_2_jackson_advanced.demos.de03;

public class GameAsset {

    // === Fields ===
    private final int    assetId;
    private final String assetName;
    private final String assetType;
    private final int    fileSize;
    private final byte[] assetData;

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

        this.assetId   = assetId;
        this.assetName = assetName.trim();
        this.assetType = assetType.trim().toLowerCase();
        this.fileSize  = fileSize;
        this.assetData = assetData;
    }

    // === Public API ===
    public int    getAssetId()   { return assetId; }
    public String getAssetName() { return assetName; }
    public String getAssetType() { return assetType; }
    public int    getFileSize()  { return fileSize; }
    public byte[] getAssetData() { return assetData; }
}
