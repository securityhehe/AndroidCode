// IMService.aidl
package com.octopus.test.server.pool;

// Declare any non-default types here with import statements

interface IMService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void sendMessage(String message);
     void receiceMesage(String message);
     void connect(String message);
     void disConnect();
     void onError(String errorMessage, int errorCode);
}
