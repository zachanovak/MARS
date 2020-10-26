package tests;

import mars.MarsLaunch;

public class TestGlobals {
    // Needs to be initialized so the tests can access Globals
    public static MarsLaunch marsLaunch;

    // Used to restore existing values after tests mess with the values
    public static boolean registerNameConstraint;
    public static boolean commaConstraint;
    public static boolean offsetConstraint;
    public static boolean registerUsage;

    public static boolean instructionSubset;
    public static boolean whitelist;
    public static String instructionSubsetString;

    public static int tabSize;

    public static void initMarsLaunch() {
        if (marsLaunch == null)
            marsLaunch = new MarsLaunch(new String[] {});
    }
}
