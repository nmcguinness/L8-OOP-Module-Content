package t21_json_2_jackson_advanced.exercises.ex05;

// Everything about an asset except its bytes: cheap to list
public record AssetMetadata(int id, String name) { }
