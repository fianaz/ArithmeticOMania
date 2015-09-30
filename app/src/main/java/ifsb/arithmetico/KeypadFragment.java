package ifsb.arithmetico;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ifsb.arithmetico.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KeypadFragment.KeypadListener} interface
 * to handle interaction events.
 * Use the {@link KeypadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeypadFragment extends Fragment {
    private TextView tv_number;
    private Button btn_enter;
    private Button[] btn_keys;
    private Button btn_back, btn_reset;
    private boolean disabled;
    private KeypadListener mListener;

    public static KeypadFragment newInstance(String param1, String param2) {
        KeypadFragment fragment = new KeypadFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    public KeypadFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keypad, container, false);

        btn_keys = new Button[10];
        btn_keys[0] = (Button) view.findViewById(R.id.key_0);
        btn_keys[1] = (Button) view.findViewById(R.id.key_1);
        btn_keys[2] = (Button) view.findViewById(R.id.key_2);
        btn_keys[3] = (Button) view.findViewById(R.id.key_3);
        btn_keys[4] = (Button) view.findViewById(R.id.key_4);
        btn_keys[5] = (Button) view.findViewById(R.id.key_5);
        btn_keys[6] = (Button) view.findViewById(R.id.key_6);
        btn_keys[7] = (Button) view.findViewById(R.id.key_7);
        btn_keys[8] = (Button) view.findViewById(R.id.key_8);
        btn_keys[9] = (Button) view.findViewById(R.id.key_9);
        for (int i=0; i < 10; i++) {
            btn_keys[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (disabled)
                        return true;


                    buttonKeyPress(view, false, false);
                    Handler.Callback callback = new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message message) {
                            enterKeyPress(true, true);
                            return true;
                        }
                    };
                    final Handler handler = new Handler(callback);
                    TimerTask timerTask = new TimerTask() {
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    };
                    (new Timer()).schedule(timerTask, 100);
                    return true;
                }
            });
            btn_keys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disabled)
                        return;

                    buttonKeyPress(v, true, true);
/*                    SoundPlayer.vibrate();
                    SoundPlayer.play(getActivity(), R.raw.keyclick);
                    String str = tv_number.getText().toString();
                    if (str.equals("0"))
                        str = "";
                    tv_number.setText(str + ((Button) v).getText());
                    */
                }
            });
        }

        btn_back = (Button) view.findViewById(R.id.key_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disabled)
                    return;

                SoundPlayer.vibrate();
                SoundPlayer.play(getActivity(), R.raw.buttonpress);
                String str = tv_number.getText().toString();
                if (!str.isEmpty())
                    tv_number.setText((str.length() == 1) ? "0" : str.substring(0, str.length() - 1));
            }
        });

        btn_reset = (Button) view.findViewById(R.id.key_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disabled)
                    return;

                SoundPlayer.vibrate();
                SoundPlayer.play(getActivity(), R.raw.buttonpress);
                reset();
            }
        });

        btn_enter = (Button) view.findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disabled)
                    return;
                enterKeyPress(true, true);
/*
                SoundPlayer.vibrate();
                SoundPlayer.play(getActivity(), R.raw.buttonpress);
                mListener.onKeypadInteraction(Integer.parseInt(tv_number.getText().toString()));
 */
            }
        });

        tv_number = (TextView) view.findViewById(R.id.tv_number);
        reset();

        return view;
    }

    private void enterKeyPress(boolean vibrate, boolean sound) {
        if (vibrate)
            SoundPlayer.vibrate();
        if (sound)
            SoundPlayer.play(getActivity(), R.raw.buttonpress);

        mListener.onKeypadInteraction(Integer.parseInt(tv_number.getText().toString()));
    }

    private void buttonKeyPress(View v, boolean vibrate, boolean sound) {
        if (vibrate)
            SoundPlayer.vibrate();
        if (sound)
            SoundPlayer.play(getActivity(), R.raw.keyclick);

        String str = tv_number.getText().toString();
        if (str.equals("0"))
            str = "";
        tv_number.setText(str + ((Button) v).getText());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (KeypadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement KeypadListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface KeypadListener {
        public void onKeypadInteraction(int number);
    }

    public void setDisabled(boolean status) {
        disabled = status;
    }

    public void reset() {
        tv_number.setText("0");
    }

    public String getCurrentValue() {
        return tv_number.getText().toString();
    }

    public void setCurrentValue(String val) {
        tv_number.setText(val);
    }
}
