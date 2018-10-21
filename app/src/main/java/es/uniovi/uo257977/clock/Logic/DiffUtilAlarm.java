package es.uniovi.uo257977.clock.Logic;

import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.DiffUtil;

public class DiffUtilAlarm extends DiffUtil.Callback {

    List<Alarm> oldList;
    List<Alarm> newList;

    public DiffUtilAlarm(List<Alarm> oldList, List<Alarm> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getID() + oldList.get(oldItemPosition).getNombre(), newList.get(newItemPosition).getID() + newList.get(newItemPosition).getNombre());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
