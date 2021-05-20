package gachon.termproject.danggeun.Util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Firestore 관렴 함수
 */
public class Firestore {
    /**
     * 현재 로그인한 유저 가져오기
     */
    public static FirebaseUser getFirebaseUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user;
    }

    /**
     *  Firestore 인스턴스 반환
     *  */
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    /**
     * 가게 list 가져오기
     */

    /**
     * 특정 가게 정보 가져오기
     */
    public static Task<QuerySnapshot> getStoreInfo(String marketTitle){
        return getFirestoreInstance().collection("ShopList").whereEqualTo("markerTitle",marketTitle).get();
    }

    /**
     * 특정 가게, 빵 list 가져오기
     */
    public static Task<QuerySnapshot> getBreadList(String storeId){
        return getFirestoreInstance().collection("Bread").whereEqualTo("storeId", storeId).get();
    }

    /**
     * 특정 빵 정보 가져오기
     */
    public static Task<DocumentSnapshot> getBreadInfo(String breadId){
        return getFirestoreInstance().collection("Bread").document(breadId).get();
    }

    /**
     * @author 최우석
     */

    /**
     * 예약 정보 가져오기 (User, arrary)
     */

    /**
     * 사진 -> url
     */

    //관리자 버전's

}
