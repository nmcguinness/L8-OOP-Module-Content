package t05_collections_1.demos.de03;

// by default an enum is just an integer e.g. Single = 0, Burst = 1
// it costs me 4 bytes to use an instance of a firemode in a class (e.g. in MachineGun)
public enum FireMode{
    Single,  //"Single" //cost? 6 chars + "/0" = 8 chars = Using ASCII = 8 x 1byte
    Burst,
    Full
}
