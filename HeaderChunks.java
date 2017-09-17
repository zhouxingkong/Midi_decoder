package MIDI;

public class HeaderChunks {
	private byte[] midiId;
	// ����
	private int length;
	// ��ʽ
	private int format;
	// track ������
	private int trackNumber;
	// MIDI ��ʱ������
	private int midiTimeSet;

	public HeaderChunks() {
		this.midiId = new byte[4];
	}

	public byte[] getMidiId() {
		return midiId;
	}

	public void setMidiId(byte[] midiId) {
		this.midiId = midiId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public int getMidiTimeSet() {
		return midiTimeSet;
	}

	public void setMidiTimeSet(int midiTimeSet) {
		this.midiTimeSet = midiTimeSet;
	}
}