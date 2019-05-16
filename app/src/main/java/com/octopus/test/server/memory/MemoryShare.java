package com.octopus.test.server.memory;

import android.os.MemoryFile;

import java.io.IOException;

public class MemoryShare {
    public void test() throws IOException {
        MemoryFile memoryFile = new MemoryFile("aa",100000) ;
        String str = "aaaaaaaa";
        byte [] b = new byte[str.length()] ;
        memoryFile.writeBytes(b,0,2000,str.length());
        memoryFile.readBytes(b,2000,0,str.length());
    }
}
