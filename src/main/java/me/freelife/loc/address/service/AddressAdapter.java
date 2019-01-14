package me.freelife.loc.address.service;

import me.freelife.loc.address.domain.Address;
import me.freelife.loc.address.domain.ApiType;
import me.freelife.loc.commons.ApiTypeName;
import me.freelife.loc.external.kakao.address.domain.KakaoResKeyword;
import me.freelife.loc.external.naver.address.domain.NaverResKeyword;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 주소 API 변환
 * 설정된 타입에 따라 분기하여 공통 주소 형식으로 맵핑
 * @param <T>
 */
@Component
public class AddressAdapter<T> {

    public Address externalAddressConverter(ResponseEntity<T> resData, ApiType type) {

        Address address = Address.builder().build();
        List<Address.AddressDocuments> list = new LinkedList<>();

        String apiType = String.valueOf(type);

        switch (apiType){
            case ApiTypeName.KAKAO:
                ResponseEntity<KakaoResKeyword> kakaoData = (ResponseEntity<KakaoResKeyword>) resData;

                KakaoResKeyword.Meta kakaoMeta = kakaoData.getBody().getMeta();
                ArrayList<KakaoResKeyword.Documents> kakaoDoc = kakaoData.getBody().getDocuments();
                kakaoDoc.forEach(i ->
                        listMaker(list, i.getAddressName(), i.getRoadAddressName(), i.getLongitude(), i.getLatitude(), i.getDistance(), i.getPlaceName(), i.getCategoryName(), i.getPhone(), i.getPlaceUrl())
                );

                address = address.builder()
                        .type(type)
                        .totalCount(kakaoMeta.getTotalCount())
                        .pageableCount(kakaoMeta.getPageableCount())
                        .isEnd(kakaoMeta.isEnd())
                        .address(list).build();
            break;
            case ApiTypeName.NAVER:
                ResponseEntity<NaverResKeyword> naverData = (ResponseEntity<NaverResKeyword>) resData;

                NaverResKeyword.Meta naverMeta = naverData.getBody().getMeta();
                ArrayList<NaverResKeyword.Documents> naverDoc = naverData.getBody().getDocuments();
                naverDoc.forEach(i ->
                        listMaker(list, i.getAddressName(), i.getRoadAddressName(), i.getLongitude(), i.getLatitude(), i.getDistance(), i.getPlaceName(), i.getCategoryName(), i.getPhone(), i.getPlaceUrl())
                );

                address = address.builder()
                        .type(type)
                        .totalCount(naverMeta.getTotalCount())
                        .pageableCount(naverMeta.getPageableCount())
                        .isEnd(naverMeta.isEnd())
                        .address(list).build();
                break;
            case ApiTypeName.GOOGLE:
                ResponseEntity<KakaoResKeyword> googleData = (ResponseEntity<KakaoResKeyword>) resData;

                KakaoResKeyword.Meta googleMeta = googleData.getBody().getMeta();
                ArrayList<KakaoResKeyword.Documents> googleDoc = googleData.getBody().getDocuments();
                googleDoc.forEach(i ->
                        listMaker(list, i.getAddressName(), i.getRoadAddressName(), i.getLongitude(), i.getLatitude(), i.getDistance(), i.getPlaceName(), i.getCategoryName(), i.getPhone(), i.getPlaceUrl())
                );

                address = address.builder()
                        .type(type)
                        .totalCount(googleMeta.getTotalCount())
                        .pageableCount(googleMeta.getPageableCount())
                        .isEnd(googleMeta.isEnd())
                        .address(list).build();
                break;
        }

        return address;
    }

    private boolean listMaker(List<Address.AddressDocuments> list, String addressName, String roadAddressName, String longitude, String latitude, String distance, String placeName, String categoryName, String phone, String placeUrl) {
        return list.add(
                Address.AddressDocuments.builder()
                        .addressName(addressName)
                        .roadAddressName(roadAddressName)
                        .longitude(longitude)
                        .latitude(latitude)
                        .distance(distance)
                        .placeName(placeName)
                        .categoryName(categoryName)
                        .phone(phone)
                        .placeUrl(placeUrl)
                        .build()
        );
    }
}
