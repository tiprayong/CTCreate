package com.comtip.tip.ctcreate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by TipRayong on 12/10/2560 14:17
 * CTCreate
 */

public class ArchivePresenter {
    MainActivity mainActivity;
    public String tempKeyword = "";


    public ArchivePresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /*--------------------------------------------------------------------------------------
      Method: setupArchiveRV()
      Description:  Setup Archive RecyclerView
    -------------------------------------------------------------------------------------*/
    public void setupArchiveRV() {
        mainActivity.saveBT.setEnabled(false);
        mainActivity.isArchive = false;

        final String[] archiveArray = mainActivity.critiqueDatabaseHelper.getAllArchive(mainActivity.mSqLiteDatabase);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        mainActivity.critiqueRV.setLayoutManager(linearLayoutManager);

        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(mainActivity, archiveArray);
        mainActivity.critiqueRV.setAdapter(adapter);

        // Select and Show  Archive
        adapter.setOnItemClickListener(new CustomRecyclerViewAdapter.OnItem_ClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!archiveArray[position].equalsIgnoreCase("null")) {
                    mainActivity.mCritique = new CritiqueClass();
                    mainActivity.mCritique = mainActivity.critiqueDatabaseHelper.getArchive(mainActivity.mSqLiteDatabase, archiveArray[position]);
                    archiveCriticalThinkingCreate(mainActivity.mCritique.get_keyword());
                }
            }
        });

        //  Delete  Archive
        adapter.setOnItemLongClickListener(new CustomRecyclerViewAdapter.OnItem_LongClickListener() {
            @Override
            public void onItemLongClickListener(int position) {
                if (!archiveArray[position].equalsIgnoreCase("null")) {
                    mainActivity.mCritique = new CritiqueClass();
                    mainActivity.mCritique = mainActivity.critiqueDatabaseHelper.getArchive(mainActivity.mSqLiteDatabase, archiveArray[position]);

                    AlertDialog.Builder alertDelete = new AlertDialog.Builder(mainActivity);
                    alertDelete.setTitle("Delete " + mainActivity.mCritique.get_keyword() + " ?");
                    alertDelete.setPositiveButton("✔ YES ✔", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainActivity.customSnackBar("Delete " + mainActivity.mCritique.get_keyword());
                            mainActivity.critiqueDatabaseHelper.deleteCritique(mainActivity.mSqLiteDatabase, mainActivity.mCritique.get_keyword());
                            setupArchiveRV();
                        }
                    });

                    alertDelete.setNegativeButton("✘ NO ✘", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel
                        }
                    });
                    AlertDialog alertD = alertDelete.create();
                    alertD.show();
                }
            }
        });
    }

    /*--------------------------------------------------------------------------------------
      Method: archiveCriticalThinkingCreate(String keyword)
      Description:  Create Question from keyword
    -------------------------------------------------------------------------------------*/
    public void archiveCriticalThinkingCreate(String keyword) {
        tempKeyword = keyword;
        keyword = "<font color='#00FF00'><i>" + keyword + "</i></font>";
        String[] critiqueArray = new String[7];
        critiqueArray[0] = "รู้ได้อย่างไรว่า " + keyword + " ?";
        critiqueArray[1] = keyword + " ทำไมถึงเป็นเช่นนั้น ?";
        critiqueArray[2] = keyword + " ทำได้อย่างไร ?";
        critiqueArray[3] = "ถ้า " + keyword + " แล้วจะเกิดอะไรขึ้น ?";
        critiqueArray[4] = keyword + " มีอะไรน่าสนใจเพิ่มอีก ?";
        critiqueArray[5] = "เพิ่มเติมประเด็น " + keyword;
        critiqueArray[6] = "ค้นหาข้อมูล " + keyword;
        mainActivity.isArchive = true;
        mainActivity.saveBT.setText("update");
        mainActivity.saveBT.setEnabled(true);
        mainActivity.critiquePresenter.setupCritiqueRV(critiqueArray, tempKeyword);
    }

}
