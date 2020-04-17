package kz.aspan.awesomechat.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;

public class EditTextMask {
    private EditTextMask() {
    }

    public static void applySelection(final EditText field, char i, boolean cursor) {
        int count = 0;
        for (char c : field.getText().toString().toCharArray()) {
            if (c == i) break;
            count++;
        }
        final int finalCount = count;
        if (!cursor) {
            field.post(new Runnable() {
                @Override
                public void run() {
                    field.setSelection(finalCount);
                }
            });
        } else {
            field.post(new Runnable() {
                @Override
                public void run() {
                    if (finalCount < field.getSelectionEnd()) {
                        field.setSelection(finalCount);
                    }
                }
            });
        }
    }

    public static String getNumber(MaskImpl mask, char placeHolder, String number) {
        Slot slot = mask.iterator().next();
        StringBuilder builder = new StringBuilder(number.length());

        for (char letter : number.toCharArray()) {
            if (letter != placeHolder && !slot.hasTag(slot.TAG_DECORATION)) {
                builder.append(letter);
            }
            slot = slot.getNextSlot();
        }
        return builder.toString();
    }

    public static void check(final TextInputLayout inputLayout, String number, int numberLength) {
        if (number.length() == numberLength) {
        } else {
            System.out.println(number.length());
            inputLayout.setError("Field number");
            inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        inputLayout.setError(null);
                    } else {
                        inputLayout.setError("Field number");
                    }
                }
            });
        }
    }


    public static final Slot[] KZ_PHONE_NUMBER = {
            PredefinedSlots.hardcodedSlot('+'),
            PredefinedSlots.hardcodedSlot('7'),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.hardcodedSlot('(').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot(')').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
    };

    public static final Slot[] CODE = {

            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),

    };
}
