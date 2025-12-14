public class MIDItools {

    /* Aufgabe 1:*/
    public static byte getNote(char note, int octave, boolean sharp) {
        int baseOffset;

        switch (note) {
            case 'C': baseOffset = 0; break;
            case 'D': baseOffset = 2; break;
            case 'E': baseOffset = 4; break;
            case 'F': baseOffset = 5; break;
            case 'G': baseOffset = 7; break;
            case 'A': baseOffset = 9; break;
            case 'B': baseOffset = 11; break;
            default:
                return 0;
        }

        int midiValue = (octave + 1) * 12 + baseOffset;
        if (sharp) {
            midiValue += 1;
        }

        if (midiValue < 0 || midiValue > 127) {
            return 0;
        }

        return (byte) midiValue;
    }

    /* Aufgabe 2: getHeader */
    public static byte[] getHeader(byte speed) {
        return new byte[] {
            (byte) 0x4D, (byte) 0x54, (byte) 0x68, (byte) 0x64,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06,
            (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x01,
            (byte) 0x00,
            speed
        };
    }

    /*
       Aufgabe 3*/
    public static byte[] getNoteEvent(byte delay, boolean noteOn, byte note, byte velocity) {
        byte status = noteOn
                ? (byte) 0b10010000  
                : (byte) 0b10000000; 

        return new byte[] {
            delay,
            status,
            note,
            velocity
        };
    }

    /* Aufgabe 4:*/
    public static byte[] addNoteToTrack(byte[] trackdata, byte[] noteEvent) {
        byte[] result = new byte[trackdata.length + noteEvent.length];
        System.arraycopy(trackdata, 0, result, 0, trackdata.length);
        System.arraycopy(noteEvent, 0, result, trackdata.length, noteEvent.length);
        return result;
    }

      /*Aufgabe 5:*/ 
    public static byte[] getTrack(byte instrument, byte[] trackdata) {

        byte[] header = new byte[] {
            (byte) 0x4D, (byte) 0x54, (byte) 0x72, (byte) 0x6B 
        };

        byte[] tempoAndTime = new byte[] {
            (byte) 0x00, (byte) 0xFF, (byte) 0x58, (byte) 0x04,
            (byte) 0x04, (byte) 0x02, (byte) 0x18, (byte) 0x08,
            (byte) 0x00, (byte) 0xFF, (byte) 0x51, (byte) 0x03,
            (byte) 0x07, (byte) 0xA1, (byte) 0x20
        };

        byte[] instrumentEvent = new byte[] {
            (byte) 0x00, (byte) 0xC0, instrument
        };

        byte[] trackEnd = new byte[] {
            (byte) 0xFF, (byte) 0x2F, (byte) 0x00
        };

        
        int length = tempoAndTime.length + instrumentEvent.length + trackdata.length;

        byte[] lengthBytes = new byte[] {
            (byte) ((length >> 24) & 0xFF),
            (byte) ((length >> 16) & 0xFF),
            (byte) ((length >> 8) & 0xFF),
            (byte) (length & 0xFF)
        };

        byte[] result = new byte[
            header.length +
            lengthBytes.length +
            length +
            trackEnd.length
        ];

        int pos = 0;

        System.arraycopy(header, 0, result, pos, header.length);
        pos += header.length;

        System.arraycopy(lengthBytes, 0, result, pos, lengthBytes.length);
        pos += lengthBytes.length;

        System.arraycopy(tempoAndTime, 0, result, pos, tempoAndTime.length);
        pos += tempoAndTime.length;

        System.arraycopy(instrumentEvent, 0, result, pos, instrumentEvent.length);
        pos += instrumentEvent.length;

        System.arraycopy(trackdata, 0, result, pos, trackdata.length);
        pos += trackdata.length;

        System.arraycopy(trackEnd, 0, result, pos, trackEnd.length);

        return result;
    }
}

