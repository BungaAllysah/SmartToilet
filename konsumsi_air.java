package com.example.tugasakhir;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evrencoskun.tableview.TableView;
import com.example.tugasakhir.adapter.TableAdapter;
import com.example.tugasakhir.tablemodel.Cell;
import com.example.tugasakhir.tablemodel.ColumnHeader;
import com.example.tugasakhir.tablemodel.RowHeader;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class konsumsi_air extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsumsi_air);

        TextInputLayout tilTanggal = findViewById(R.id.til_piltanggal);
        tilTanggal.setEndIconOnClickListener(view -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener((epoch) ->{
                String tanggal = LocalDate.from(Instant.ofEpochMilli(epoch).atOffset(OffsetDateTime.now().getOffset()))
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
                tilTanggal.getEditText().setText(tanggal);
            });
            datePicker.show(getSupportFragmentManager(), null);
        });

        TextInputLayout pilKereta = findViewById(R.id.til_pilkereta);
        ((AutoCompleteTextView) pilKereta.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Joglosemarkerto", "Taksaka", "Gajayana Premium"
                })
        );

        TextInputLayout pilGerbong = findViewById(R.id.til_pilgerbong);
        ((AutoCompleteTextView) pilGerbong.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Gerbong 1", "Gerbong 2", "Gerbong 3", "gerbong 4"
                })
        );

        List<RowHeader> rowHeaders = new ArrayList<>();

        List<ColumnHeader> columnHeaders = new ArrayList<>();
        Collections.addAll(columnHeaders,
                new ColumnHeader("Waktu"),
                new ColumnHeader("Volume Akhir"),
                new ColumnHeader("Presentase Penggunaan")
        );

        List<List<Cell>> cells = new ArrayList<>();
        List<Cell> cell1 = new ArrayList<>();
        Collections.addAll(cell1, new Cell("17:00"), new Cell("70 L"), new Cell("30 %"));
        List<Cell> cell2 = new ArrayList<>();
        Collections.addAll(cell2, new Cell("17:00"), new Cell("70 L"), new Cell("30 %"));
        Collections.addAll(cells, cell1, cell2);

        TableView table = findViewById(R.id.table_monitoring_konsumair);
        TableAdapter adapter = new TableAdapter();
        table.setAdapter(adapter);
        adapter.setAllItems(columnHeaders, rowHeaders, cells);
    }
}