package gachon.termproject.danggeun.Util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import gachon.termproject.danggeun.Util.Model.ReservatoinRequest;

/**
 * Firestore 관렴 함수
 */
public class Firebase {
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

    public static FirebaseDatabase getFirebaseDatabaseInstance(){
        return FirebaseDatabase.getInstance();
    }


    /**
     * 특정 가게 정보 가져오기
     */
    public static Task<QuerySnapshot> getStoreInfo(String marketTitle){
        return getFirestoreInstance().collection("ShopList").whereEqualTo("markerTitle",marketTitle).get();
    }
    public static Task<DocumentSnapshot> getStorebyId(String storeId){
        return getFirestoreInstance().collection("ShopList").document(storeId).get();
    }
    /**
     * 모든 가게 가져오기
     * @return
     */
    public static Task<QuerySnapshot> getStores(){
        return getFirestoreInstance().collection("ShopList").get();
    }


    /**
     * 특정 가게, 빵 list 가져오기
     */
    public static Task<QuerySnapshot> getBreadList(String storeId){
        return getFirestoreInstance().collection("Bread").whereEqualTo("storeID", storeId).get();
    }


    /**
     * 특정 빵 정보 가져오기
     */
    public static Task<DocumentSnapshot> getBreadInfo(String breadId){
        return getFirestoreInstance().collection("Bread").document(breadId).get();
    }

    /**
     * 예약 하기
     */
    public static Task<Void> addReservation(ReservatoinRequest request){
        // userId + 현재 시간으로 documentID 생성
        return getFirestoreInstance().collection("Reservation").document(request.getUserId() + System.currentTimeMillis()).set(request);
    }

    /**
     * @author 최우석
     */
    public static Task<QuerySnapshot> getReservationShopList(String userId){
        return getFirestoreInstance().collection("Reservation").whereEqualTo("userId",userId).get();
    }

    /**
     * 예약 삭제 하기
     */
    public static Task<Void> updateReservationStatus(String rId){
        return getFirestoreInstance().collection("Reservation").document(rId).update("status", false);
    }

    /**
     * 사진 -> url
     */

    //관리자 버전's

    //가게에 예약된 리스트 가져오기
    public static Task<QuerySnapshot> getReservation(String storeId) {
        return getFirestoreInstance().collection("Reservation").whereEqualTo("storeId", storeId).get();
    }

    //유저 정보 가져오기
    public static Task<DocumentSnapshot> getUser(String userId) {
        return getFirestoreInstance().collection("user").document(userId).get();
    }

}
