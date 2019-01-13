package me.freelife.loc.address.domain;

import lombok.*;

import java.util.List;

/**
   @FileName  : Address.java
   @Description : 지번주소 상세 정보
   @author      : KMS
   @since       : 2017. 8. 10.
   @version     : 1.0

   @개정이력

   수정일           수정자         수정내용
   -----------      ---------      -------------------------------
   2017. 8. 10.     KMS            최초생성

 */
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Address {

    private List<AddressDocuments> address;

    private ApiType type = ApiType.KAKAO;

    /** 매칭된 문서수 */
    private Integer totalCount;

    /** 노출가능한 문서수 */
    private Integer pageableCount;

    /** 현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음. */
    private boolean isEnd;

    @Builder @NoArgsConstructor @AllArgsConstructor
    @Getter @Setter
    public static class AddressDocuments {

        /** 전체 지번 주소 */
        private String addressName;

        /** 전체 도로명 주소 */
        private String roadAddressName;

        /** X 좌표값 혹은 longitude */
        private String longitude;

        /** Y 좌표값 혹은 latitude */
        private String latitude;

        /** 중심좌표까지의 거리(x,y 파라미터를 준 경우에만 존재). 단위 meter */
        private String distance;

        /** 장소명, 업체명 */
        private String placeName;

        /** 카테고리 이름 */
        private String categoryName;

        /** 전화번호 */
        private String phone;

        /** 장소 상세페이지 URL */
        private String placeUrl;
    }


}
