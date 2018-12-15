package com.example.rima.francais;

//represnted english and french translation for each word
public class Word {
//    variables or states
//    English word translation

    private String mDefaultTranslation;

//    french word translation

    private String mFrenchTranslation;


//    state for sound
    private int mAudioResourceId;

//    constructots for english and french translaton

    public Word(String defaultTranslation, String frenchTranslation, int audioResourceId) {

        mDefaultTranslation = defaultTranslation;
        mFrenchTranslation = frenchTranslation;
        mAudioResourceId = audioResourceId;
    }


//    object or method

//    get english translation method

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

//    get french translation method

    public String getFrenchTranslation() {
        return mFrenchTranslation;
    }


    // get audio method
    public int getAudio() {
        return mAudioResourceId;
    }


}



