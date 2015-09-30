package ifsb.arithmetico;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ifsb.arithmetico.R;
import ifsb.arithmetico.core.Question;


public class DivisionPanelFragment extends QuestionPanelFragment {
    private Session session;
    private QuestionPanelFragment.OnQuestionPanelListener mListener;
//    private DivisionQuestion q;

    public static DivisionPanelFragment newInstance() {
        DivisionPanelFragment fragment = new DivisionPanelFragment();
        return fragment;
    }

    public DivisionPanelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_division_panel, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (QuestionPanelFragment.OnQuestionPanelListener) activity;
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
//        q = (DivisionQuestion) question;
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
