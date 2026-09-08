package com.example.taskmaster;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TareaAdapter
        extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    public interface OnTareaCambiadaListener {
        void onTareaCambiada();
    }

    private List<Tarea> listaTareas;
    private OnTareaCambiadaListener listener;

    public TareaAdapter(
            List<Tarea> listaTareas,
            OnTareaCambiadaListener listener) {

        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View vista =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_tarea,
                                parent,
                                false
                        );

        return new TareaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TareaViewHolder holder,
            int position) {

        Tarea tarea = listaTareas.get(position);

        holder.txtNombre.setText(
                tarea.getNombre()
        );

        holder.txtDescripcion.setText(
                tarea.getDescripcion()
        );

        holder.txtPrioridad.setText(
                obtenerTextoPrioridad(
                        tarea.getPrioridad()
                )
        );

        holder.txtCategoria.setText(
                "📂 " + tarea.getCategoria()
        );

        holder.chkCompletada.setOnCheckedChangeListener(
                null
        );

        holder.chkCompletada.setChecked(
                tarea.isCompletada()
        );

        actualizarApariencia(
                holder,
                tarea.isCompletada()
        );

        holder.chkCompletada.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    tarea.setCompletada(isChecked);

                    actualizarApariencia(
                            holder,
                            isChecked
                    );

                    if (listener != null) {
                        listener.onTareaCambiada();
                    }
                }
        );
    }

    private void actualizarApariencia(
            TareaViewHolder holder,
            boolean completada) {

        if (completada) {

            holder.txtNombre.setPaintFlags(
                    holder.txtNombre.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG
            );

            holder.txtNombre.setAlpha(0.5f);
            holder.txtDescripcion.setAlpha(0.5f);

        } else {

            holder.txtNombre.setPaintFlags(
                    holder.txtNombre.getPaintFlags()
                            & ~Paint.STRIKE_THRU_TEXT_FLAG
            );

            holder.txtNombre.setAlpha(1f);
            holder.txtDescripcion.setAlpha(1f);
        }
    }

    private String obtenerTextoPrioridad(
            String prioridad) {

        if (prioridad.equals("Alta")) {

            return "🔴 Alta";

        } else if (prioridad.equals("Baja")) {

            return "🟢 Baja";

        } else {

            return "🟡 Media";
        }
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class TareaViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtDescripcion;
        TextView txtPrioridad;
        TextView txtCategoria;
        CheckBox chkCompletada;

        public TareaViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtNombre =
                    itemView.findViewById(
                            R.id.txtNombreItem
                    );

            txtDescripcion =
                    itemView.findViewById(
                            R.id.txtDescripcionItem
                    );

            txtPrioridad =
                    itemView.findViewById(
                            R.id.txtPrioridadItem
                    );

            txtCategoria =
                    itemView.findViewById(
                            R.id.txtCategoriaItem
                    );

            chkCompletada =
                    itemView.findViewById(
                            R.id.chkCompletadaItem
                    );
        }
    }
}