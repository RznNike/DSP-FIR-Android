package com.dsp.fir.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartsParametersStorage {
    private static ChartsParametersStorage instance;

    private int samplingFrequency;
    private int firstSignalFrequency;
    private int secondSignalFrequency;
    private int counts;
    private double[] impulseResponse;
    private boolean drawFirstSignal;
    private boolean drawSecondSignal;
    private boolean drawSumSignal;
    private boolean drawImpulseResponse;
    private boolean drawFilteredSignal;
    private boolean drawFirstSignalWithOffset;

    private ChartsParametersStorage() {
        samplingFrequency = 1000;
        firstSignalFrequency = 100;
        secondSignalFrequency = 150;
        counts = 100;
        drawFirstSignal = true;
        drawSecondSignal = true;
        drawSumSignal = true;
        drawImpulseResponse = true;
        drawFilteredSignal = true;
        drawFirstSignalWithOffset = true;
        fillDefaultImpulseResponse();
    }

    public static ChartsParametersStorage getInstance() {
        if (instance == null) {
            instance = new ChartsParametersStorage();
        }
        return instance;
    }

    public void fillDefaultImpulseResponse() {
        impulseResponse = new double[] {
                -0.0003841083226338397,
                -0.000096924454953799442,
                0.00092450687410056023,
                0.0030529444711698151,
                0.0059056214583988976,
                0.0082917830000227549,
                0.0085573916343787849,
                0.0054651560980227155,
                -0.00077030494802866152,
                -0.0079479710164444065,
                -0.012427175515902823,
                -0.010824675890325474,
                -0.0021905007650409513,
                0.010553150057839515,
                0.021054382209008977,
                0.022189579050458121,
                0.010030486575831311,
                -0.012802862224627067,
                -0.036448493931204563,
                -0.046791073236053415,
                -0.031362025078656861,
                0.014314489975280426,
                0.083023442801575295,
                0.15698279589373101,
                0.21383097759789124,
                0.23514872920268504,
                0.21383097759789124,
                0.15698279589373101,
                0.083023442801575295,
                0.014314489975280426,
                -0.031362025078656861,
                -0.046791073236053415,
                -0.036448493931204563,
                -0.012802862224627067,
                0.010030486575831311,
                0.022189579050458121,
                0.021054382209008977,
                0.010553150057839515,
                -0.0021905007650409513,
                -0.010824675890325474,
                -0.012427175515902823,
                -0.0079479710164444065,
                -0.00077030494802866152,
                0.0054651560980227155,
                0.0085573916343787849,
                0.0082917830000227549,
                0.0059056214583988976,
                0.0030529444711698151,
                0.00092450687410056023,
                -0.000096924454953799442,
                -0.0003841083226338397};
    }
}
