package com.example.forgecareer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forgecareer.decorators.MySelectorDecorator;
import com.example.forgecareer.decorators.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;

import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InterviewFragment#} factory method to
 * create an instance of this fragment.
 */
public class InterviewFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

    MaterialCalendarView calendarView;
    TextView textView;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();



    public InterviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_interview, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        textView = view.findViewById(R.id.textViewDateSelection);

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnDateLongClickListener(this);
        calendarView.setOnMonthChangedListener(this);
        textView.setText("No Selection");

        calendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                oneDayDecorator
        );
        return view;
    }

    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView calendarView,
            @NonNull CalendarDay date,
            boolean selected) {
        LocalDate localDate = date.getDate();
        oneDayDecorator.setDate(date.getDate());
        calendarView.invalidateDecorators();

        textView.setText(date.getDate().toString());

    }

    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView calendarView, @NonNull CalendarDay date) {
//        final String text = String.format("%s is available", FORMATTER.format(date.getDate()));
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        textView.setText("No onDateLongClick");
    }

    @Override
    public void onMonthChanged(MaterialCalendarView calendarView, CalendarDay date) {
        //noinspection ConstantConditions
//        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
        textView.setText("onMonthChanged");
    }

}