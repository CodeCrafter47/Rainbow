package PluginReference;

/**
 * A note block
 */
public interface MC_NoteBlock {

    /**
     * Gets the note.
     *
     * @return The note
     * @see <a href="http://minecraft.gamepedia.com/Note_Block">http://minecraft.gamepedia.com/Note_Block</a>
     */
    int getNote();

    /**
     * Set the note
     *
     * @param note The note
     * @see <a href="http://minecraft.gamepedia.com/Note_Block">http://minecraft.gamepedia.com/Note_Block</a>
     */
    void setNote(int note);

    /**
     * Plays the note.
     */
    void play();

    /**
     * Plays an arbitrary note with an arbitrary instrument
     *
     * @param instrument The instrument
     * @param note      The note
     */
    void play(MC_Instrument instrument, int note);
}
