package com.dsp.fir.global.flow;

import java.util.HashMap;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Router;

public class CiceroneHolder {
    private HashMap<String, Cicerone<Router>> containers;

    public CiceroneHolder() {
        containers = new HashMap<>();
    }

    public Cicerone<Router> getCicerone(String containerTag) {
        if (!containers.containsKey(containerTag)) {
            containers.put(containerTag, Cicerone.create(new Router()));
        }
        return containers.get(containerTag);
    }
}
