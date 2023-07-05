package com.example.tugasakhir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.example.tugasakhir.R;
import com.example.tugasakhir.tablemodel.Cell;
import com.example.tugasakhir.tablemodel.ColumnHeader;
import com.example.tugasakhir.tablemodel.RowHeader;

public class TableAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    class CellViewHolder extends AbstractViewHolder {

        final TextView tvText;

        public CellViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tv_cell_text);
        }
    }

    class ColumnHeaderViewHolder extends AbstractViewHolder {

        final TextView tvText;

        public ColumnHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tv_cell_text);
        }
    }

    class RowHeaderViewHolder extends AbstractViewHolder {

        final TextView tvText;

        public RowHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tv_cell_text);
        }
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CellViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false)
        );
    }

    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell cellItemModel, int columnPosition, int rowPosition) {
        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.tvText.setText(cellItemModel.getData());
        viewHolder.tvText.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        viewHolder.tvText.requestLayout();
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColumnHeaderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false)
        );
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable ColumnHeader columnHeaderItemModel, int columnPosition) {
        ColumnHeaderViewHolder viewHolder = (ColumnHeaderViewHolder) holder;
        Context context = viewHolder.itemView.getContext();
        viewHolder.tvText.setText(columnHeaderItemModel.getData());
        viewHolder.tvText.setTextAppearance(context, com.google.android.material.R.style.TextAppearance_AppCompat_Body2);
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RowHeaderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false)
        );
    }

    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RowHeader rowHeaderItemModel, int rowPosition) {
        RowHeaderViewHolder viewHolder = (RowHeaderViewHolder) holder;
        viewHolder.tvText.setText(rowHeaderItemModel.getData());
        viewHolder.tvText.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false);
    }
}
