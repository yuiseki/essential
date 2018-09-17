package net.yuiseki.essential;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.logging.Handler;

public class EssentialAccessibilityService extends AccessibilityService {
    private static final String TAG = "EssentialAccessibilityService";
    private String lastVideoUrl;
    private String lastVideoTitle;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        String eventTypeName = "";
        switch (event.getEventType()) {
            // Notificationの表示に変更があったとき
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventTypeName = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            // View をタップしたとき
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventTypeName = "TYPE_VIEW_CLICKED";
                break;
            // View にフォーカスがあたったとき
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventTypeName = "TYPE_VIEW_FOCUSED";
                break;
            // View をロングタップしたとき
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventTypeName = "TYPE_VIEW_LONG_CLICKED";
                break;
            // View が選択されたとき
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventTypeName = "TYPE_VIEW_SELECTED";
                break;
            // View のテキストが変更されたとき
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_CHANGED";
                break;
            // 画面の表示に変更があったとき
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventTypeName = "TYPE_WINDOW_STATE_CHANGED";
                break;
            // アナウンスがあったとき
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventTypeName = "TYPE_ANNOUNCEMENT";
                break;
            // ジェスチャーが終わったとき
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                eventTypeName = "TYPE_GESTURE_DETECTION_END";
                break;
            // ジェスチャーが始まったとき
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                eventTypeName = "TYPE_GESTURE_DETECTION_START";
                break;
            // タッチ探索が終わったとき
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            // タッチ探索が始まったとき
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            // タッチ操作が終わったとき
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                eventTypeName = "TYPE_TOUCH_INTERACTION_END";
                break;
            // タッチ操作が始まったとき
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                eventTypeName = "TYPE_TOUCH_INTERACTION_START";
                break;
            // View のアクセシビリティ・フォーカスがクリアされたとき
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                eventTypeName = "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
                break;
            // View がアクセシビリティ・フォーカスされたとき
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                eventTypeName = "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
                break;
            // View のホバーが始まったとき
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventTypeName = "TYPE_VIEW_HOVER_ENTER";
                break;
            // View のホバーが終わったとき
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventTypeName = "TYPE_VIEW_HOVER_EXIT";
                break;
            // View をスクロールしたとき
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventTypeName = "TYPE_VIEW_SCROLLED";
                break;
            // View のテキスト範囲が変更されたとき
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            // View のテキストを横断したとき
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                eventTypeName = "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
                break;
            // 画面内のコンテンツが変更されたとき
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventTypeName = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT:
                break;
            case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                break;
            default:
                eventTypeName = "UNKNOWN_TYPE";

        }

        String contentChangeTypeName = "";
        switch (event.getContentChangeTypes()){
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION:
                contentChangeTypeName = "CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION";
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_PANE_TITLE:
                contentChangeTypeName = "CONTENT_CHANGE_TYPE_PANE_TITLE";
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE:
                contentChangeTypeName = "CONTENT_CHANGE_TYPE_SUBTREE";
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT:
                contentChangeTypeName = "CONTENT_CHANGE_TYPE_TEXT";
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED:
                contentChangeTypeName = "CONTENT_CHANGE_TYPE_UNDEFINED";
                break;
        }

        String packageName = (String) event.getPackageName();
        if (packageName==null
                || packageName.equals("com.android.launcher3")
                || packageName.equals("com.android.systemui")
                || packageName.equals("com.google.android.googlequicksearchbox")
                ){
            return;
        }

        Log.d(TAG, "----------");
        Log.d(TAG, "event: "+eventTypeName);
        Log.d(TAG, "content: "+contentChangeTypeName);
        Log.d(TAG, "package: "+event.getPackageName());
        Log.d(TAG, "class: "+event.getClassName());

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root==null)
            root = event.getSource();
        if (root==null)
            return;

        EssentialApplication essentialApplication = (EssentialApplication)this.getApplication();

        if (event.getPackageName().equals("com.google.android.youtube")) {
            if (essentialApplication.inspectYouTubeVideoData){
                getYouTubeTitleAndUrl(root);
            }
        }

    }

    private void getYouTubeTitleAndUrl(AccessibilityNodeInfo root){
        String sharePanelId = "com.google.android.youtube:id/unified_share_panel";
        tree(root, "");
        List<AccessibilityNodeInfo> sharePanel = root.findAccessibilityNodeInfosByViewId(sharePanelId);
        if (sharePanel.size()==0){
            String titleId = "com.google.android.youtube:id/title";
            List<AccessibilityNodeInfo> titles = root.findAccessibilityNodeInfosByViewId(titleId);
            if (titles.size()>0){
                lastVideoTitle = titles.get(0).getText().toString();
                if (lastVideoUrl==null){
                    String shareText = "共有";
                    List<AccessibilityNodeInfo> shareButton = root.findAccessibilityNodeInfosByText(shareText);
                    if (shareButton.size()>0){
                        shareButton.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        } else {
            String copyText = "コピー";
            if (sharePanel.size()>0){
                List<AccessibilityNodeInfo> copyButton = sharePanel.get(0).findAccessibilityNodeInfosByText(copyText);
                if (copyButton.size()>0){
                    copyButton.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    EssentialApplication essentialApplication = (EssentialApplication)this.getApplication();
                    essentialApplication.inspectYouTubeVideoData = false;

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                    if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "get text from clipboard");
                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                    }
                }
            }
        }
        Log.d(TAG, ""+lastVideoTitle);
        Log.d(TAG, ""+lastVideoUrl);
    }

    private AccessibilityNodeInfo tree(AccessibilityNodeInfo parent, String strAddress){
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            AccessibilityNodeInfo child = parent.getChild(i);

            if (child != null) {
                String strToAddress = strAddress+" > "+child.getViewIdResourceName();
                Log.v(TAG,"" + strToAddress);
                if (child.getText()!=null){
                    Log.v(TAG,"text = " + child.getText());
                }
                if (child.getContentDescription()!=null){
                    Log.v(TAG,"description = " + child.getContentDescription());
                }
                tree(child, strToAddress);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {

    }
}
