package net.yuiseki.essential;

import net.yuiseki.essential.view.EssentialButton;
import net.yuiseki.essential.view.EssentialTweetDialog;
import net.yuiseki.essential.view.EssentialYouTubeView;

public class EssentialEverything {
    private EssentialService essentialService;

    public EssentialButton essentialButton = null;
    int lastButtonX = 0;
    int lastButtonY = 0;
    private EssentialTweetDialog essentialTweetDialog = null;
    private EssentialYouTubeView essentialYouTubeView = null;

    public EssentialEverything(EssentialService essentialService) {
        this.essentialService = essentialService;
    }


    public void showEssentialButton(){
        if (essentialButton==null) {
            essentialButton = new EssentialButton(this.essentialService);
        }
        essentialButton.essentialWindowLayoutParams.x = lastButtonX;
        essentialButton.essentialWindowLayoutParams.y = lastButtonY;
        essentialButton.setVisibility(true);
    }

    public void hideEssentialButton(){
        if (essentialButton != null) {
            essentialButton.setVisibility(false);
            lastButtonX = essentialButton.essentialWindowLayoutParams.x;
            lastButtonY = essentialButton.essentialWindowLayoutParams.y;
        }
    }


    public void showEssentialTweetDialog(){
        hideEssentialButton();
        if (essentialTweetDialog ==null){
            essentialTweetDialog = new EssentialTweetDialog(this.essentialService);
        }
        essentialTweetDialog.setVisibility(true);
    }

    public void hideEssentialTweetDialog(){
        showEssentialButton();
        if (essentialTweetDialog !=null){
            essentialTweetDialog.setVisibility(false);
        }
        essentialTweetDialog = null;
    }


    public void showEssentialYouTubeView(){
        hideEssentialButton();
        if (essentialYouTubeView == null){
            essentialYouTubeView = new EssentialYouTubeView(this.essentialService);
        }
        essentialYouTubeView.setVisibility(true);
    }

    public void hideEssentialYouTubeView(){
        showEssentialButton();
        if (essentialYouTubeView!=null){
            essentialYouTubeView.setVisibility(false);
        }
        essentialYouTubeView = null;
    }


    public void onDestroy() {
        essentialButton = null;
        essentialTweetDialog = null;
        essentialYouTubeView = null;
    }
}
