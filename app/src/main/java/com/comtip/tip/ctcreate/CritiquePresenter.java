package com.comtip.tip.ctcreate;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TipRayong on 12/10/2560 13:06
 * CTCreate
 */

public class CritiquePresenter {
    MainActivity mainActivity;
    ChromeCustomTab chromeCustomTab;

    public CritiquePresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setupChrome() {
        chromeCustomTab = new ChromeCustomTab(mainActivity);
        chromeCustomTab.setupChromeCustomTab();
    }


    /*--------------------------------------------------------------------------------------
    Method: criticalThinkingCreate(String keyword)
    Description:  Create Question from keyword
   -------------------------------------------------------------------------------------*/
    public void criticalThinkingCreate(String keyword) {
        mainActivity.mCritique = new CritiqueClass();
        mainActivity.mCritique.set_keyword(keyword);
        mainActivity.mCritique.set_CT_0("");
        mainActivity.mCritique.set_CT_1("");
        mainActivity.mCritique.set_CT_2("");
        mainActivity.mCritique.set_CT_3("");
        mainActivity.mCritique.set_CT_4("");
        mainActivity.mCritique.set_CT_5(keyword);

        mainActivity.isArchive = false;
        mainActivity.saveBT.setText("save");
        mainActivity.saveBT.setEnabled(true);

        keyword = "<font color='#00FF00'><i>" + keyword + "</i></font>";
        String[] critiqueArray = new String[7];
        critiqueArray[0] = "รู้ได้อย่างไรว่า " + keyword + " ?";
        critiqueArray[1] = keyword + " ทำไมถึงเป็นเช่นนั้น ?";
        critiqueArray[2] = keyword + " ทำได้อย่างไร ?";
        critiqueArray[3] = "ถ้า " + keyword + " แล้วจะเกิดอะไรขึ้น ?";
        critiqueArray[4] = keyword + " มีอะไรน่าสนใจเพิ่มอีก ?";
        critiqueArray[5] = "เพิ่มเติมประเด็น " + keyword;
        critiqueArray[6] = "ค้นหาข้อมูล " + keyword;

        setupCritiqueRV(critiqueArray, mainActivity.mCritique.get_keyword());
    }


    /*--------------------------------------------------------------------------------------
      Method: setupCritiqueRV(final String[] critiqueArray, final String searchKeyword)
      Description:  Setup Critique RecyclerView
    -------------------------------------------------------------------------------------*/
    public void setupCritiqueRV(final String[] critiqueArray, final String searchKeyword) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        mainActivity.critiqueRV.setLayoutManager(linearLayoutManager);

        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(mainActivity, critiqueArray);
        mainActivity.critiqueRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomRecyclerViewAdapter.OnItem_ClickListener() {
            @Override
            public void onItemClick(int position) {

                if (position == 6) {
                    if (chromeCustomTab.isInternetConnectionAvailable(mainActivity)) {
                        chromeCustomTab.openChromeCustom("https://www.google.co.th/search?q=" + searchKeyword);
                    } else {
                        Toast.makeText(mainActivity, "Internet not available !!!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    critiqueDialog(critiqueArray[position], position);
                }
            }
        });

        adapter.setOnItemLongClickListener(new CustomRecyclerViewAdapter.OnItem_LongClickListener() {
            @Override
            public void onItemLongClickListener(int position) {
                  // no operation.
            }
        });
    }

    /*--------------------------------------------------------------------------------------
       Method: critiqueDialog(String critique, final int position)
       Description:  Show Answer
      -------------------------------------------------------------------------------------*/
    public void critiqueDialog(String critique, final int position) {

        final Dialog answerDialog = new Dialog(mainActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        answerDialog.setTitle("Answer");
        answerDialog.setContentView(R.layout.temp_layout);
        answerDialog.setCanceledOnTouchOutside(false);

        LinearLayout answerLayout = (LinearLayout) answerDialog.findViewById(R.id.tempLayout);

        final TextView empty0TV = new TextView(mainActivity);
        empty0TV.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        empty0TV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        answerLayout.addView(empty0TV);

        final TextView critiqueTV = new TextView(mainActivity);
        critiqueTV.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        critiqueTV.setGravity(Gravity.CENTER_HORIZONTAL);
        critiqueTV.setTextColor(Color.WHITE);
        critiqueTV.setText(Html.fromHtml(critique));
        critiqueTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        answerLayout.addView(critiqueTV);

        final TextView empty1TV = new TextView(mainActivity);
        empty1TV.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        empty1TV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        answerLayout.addView(empty1TV);

        final EditText answerET = new EditText(mainActivity);
        answerET.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        String tempA = "";
        switch (position) {
            case 0:
                tempA = mainActivity.mCritique.get_CT_0();
                break;

            case 1:
                tempA = mainActivity.mCritique.get_CT_1();
                break;

            case 2:
                tempA = mainActivity.mCritique.get_CT_2();
                break;

            case 3:
                tempA = mainActivity.mCritique.get_CT_3();
                break;

            case 4:
                tempA = mainActivity.mCritique.get_CT_4();
                break;

            case 5:
                tempA = mainActivity.mCritique.get_CT_5();
                break;

        }

        LinearLayout row = new LinearLayout(mainActivity);
        row.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.weight = 1;

        Button cancelSearchBT = new Button(mainActivity);
        cancelSearchBT.setLayoutParams(params);
        cancelSearchBT.setText("Cancel");
        cancelSearchBT.setBackgroundResource(R.drawable.textviewshape);
        Drawable cancelImage = mainActivity.getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel, null);
        int h1 = cancelImage.getIntrinsicHeight();
        int w1 = cancelImage.getIntrinsicWidth();
        cancelImage.setBounds(0, 0, w1, h1);
        cancelSearchBT.setCompoundDrawables(cancelImage, null, null, null);
        cancelSearchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide Keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(answerET.getWindowToken(), 0);

                answerDialog.dismiss();
            }
        });

        Button addAnswerBT = new Button(mainActivity);
        addAnswerBT.setLayoutParams(params);
        addAnswerBT.setText("Save Answer");
        addAnswerBT.setBackgroundResource(R.drawable.textviewshape);
        Drawable saveImage = mainActivity.getResources().getDrawable(android.R.drawable.ic_menu_save, null);
        int h2 = saveImage.getIntrinsicHeight();
        int w2 = saveImage.getIntrinsicWidth();
        saveImage.setBounds(0, 0, w2, h2);
        addAnswerBT.setCompoundDrawables(saveImage, null, null, null);
        addAnswerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerET.getText().toString().isEmpty()) {
                    mainActivity.customSnackBar("Please Think Carefully !!!");
                } else {
                    String temp = answerET.getText().toString();
                    switch (position) {
                        case 0:
                            mainActivity.mCritique.set_CT_0(temp);
                            break;

                        case 1:
                            mainActivity.mCritique.set_CT_1(temp);
                            break;

                        case 2:
                            mainActivity.mCritique.set_CT_2(temp);
                            break;

                        case 3:
                            mainActivity.mCritique.set_CT_3(temp);
                            break;

                        case 4:
                            mainActivity.mCritique.set_CT_4(temp);
                            break;

                        case 5:
                            mainActivity.mCritique.set_CT_5(temp);
                            break;

                    }
                    //Hide Keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(answerET.getWindowToken(), 0);

                    answerDialog.dismiss();
                }
            }
        });

        row.addView(cancelSearchBT);
        row.addView(addAnswerBT);
        answerLayout.addView(row);

        final TextView emptyTV = new TextView(mainActivity);
        emptyTV.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        emptyTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        answerLayout.addView(emptyTV);

        answerET.setText(tempA);
        answerLayout.addView(answerET);
        answerDialog.show();
    }


}