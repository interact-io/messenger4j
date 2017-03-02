package com.github.messenger4j.receive.events;

import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_ATTACHMENTS;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_ID;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_MESSAGE;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_MID;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_RECIPIENT;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_SENDER;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_TIMESTAMP;
import static com.github.messenger4j.internal.JsonHelper.getProperty;
import static com.github.messenger4j.internal.JsonHelper.getPropertyAsDate;
import static com.github.messenger4j.internal.JsonHelper.getPropertyAsString;

import com.github.messenger4j.receive.attachments.Attachment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This event will occur when a message with {@link Attachment}s (image, audio, video, file or location)
 * has been sent to your page.
 *
 * <p>
 * For further information refer to:<br>
 * <a href="https://developers.facebook.com/docs/messenger-platform/webhook-reference/message">
 * https://developers.facebook.com/docs/messenger-platform/webhook-reference/message
 * </a>
 * </p>
 *
 * @author Max Grabenhorst
 * @since 0.6.0
 * @see Event
 * @see Attachment
 */
public final class AttachmentMessageEvent extends MessageEvent {

    private final List<Attachment> attachments;

    /**
     * <b>Internal</b> method to create an instance of {@link AttachmentMessageEvent} from the given
     * event as JSON structure.
     *
     * @param jsonObject the event as JSON structure
     * @return the created {@link AttachmentMessageEvent}
     */
    public static AttachmentMessageEvent fromJson(JsonObject jsonObject) {
        final String senderId = getPropertyAsString(jsonObject, PROP_SENDER, PROP_ID);
        final String recipientId = getPropertyAsString(jsonObject, PROP_RECIPIENT, PROP_ID);
        final Date timestamp = getPropertyAsDate(jsonObject, PROP_TIMESTAMP);
        final String mid = getPropertyAsString(jsonObject, PROP_MESSAGE, PROP_MID);
        final JsonArray attachmentsJsonArray = getProperty(jsonObject, PROP_MESSAGE, PROP_ATTACHMENTS).getAsJsonArray();

        final List<Attachment> attachments = new ArrayList<>(attachmentsJsonArray.size());
        for (JsonElement attachmentJsonElement : attachmentsJsonArray) {
            attachments.add(Attachment.fromJson(attachmentJsonElement.getAsJsonObject()));
        }

        return new AttachmentMessageEvent(senderId, recipientId, timestamp, mid, attachments);
    }

    public AttachmentMessageEvent(String senderId, String recipientId, Date timestamp, String mid,
                                   List<Attachment> attachments) {

        super(senderId, recipientId, timestamp, mid);
        this.attachments = attachments == null ? null : Collections.unmodifiableList(attachments);
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AttachmentMessageEvent that = (AttachmentMessageEvent) o;
        return Objects.equals(attachments, that.attachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), attachments);
    }

    @Override
    public String toString() {
        return "AttachmentMessageEvent{" +
                "attachments=" + attachments +
                "} super=" + super.toString();
    }
}