package com.i54mpenguin.protocol.world;

import com.i54mpenguin.protocol.api.AbstractProtocolIDMapping;

public class SoundIDMapping extends AbstractProtocolIDMapping {

    private final String soundName;

    public SoundIDMapping(final int protocolVersionRangeStart, final int protocolVersionRangeEnd, final String soundName) {
        super(protocolVersionRangeStart, protocolVersionRangeEnd);
        this.soundName = soundName;
    }

    public String getSoundName() {
        return soundName;
    }

}
