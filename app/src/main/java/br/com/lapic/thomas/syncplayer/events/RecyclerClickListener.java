package br.com.lapic.thomas.syncplayer.events;

import android.view.View;

/**
 * Created by thomasmarquesbrandaoreis on 29/09/2017.
 */

public interface RecyclerClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);

}
