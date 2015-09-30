package ifsb.arithmetico;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ifsb.arithmetico.core.Question;
import ifsb.arithmetico.R;


public class MultiplicationPanelFragment extends QuestionPanelFragment {
    private Session session;
    private OnQuestionPanelListener mListener;
//    private MultiplicationQuestion q;

    public static MultiplicationPanelFragment newInstance() {
        MultiplicationPanelFragment fragment = new MultiplicationPanelFragment();
        return fragment;
    }

    public MultiplicationPanelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiplication_panel, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnQuestionPanelListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void postQuestion(Question question) {
//        q = (MultiplicationQuestion) question;
    }

    public void endSession() {
    }

    public void correctResponse() {
    }

    public void wrongResponse() {
        endSession();
    }

    public boolean checkAnswer(int answer) {
        return true;
    }

    public boolean questionDone() {
        return true;
    }

}
