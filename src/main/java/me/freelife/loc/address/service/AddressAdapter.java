package me.freelife.loc.address.service;

import me.freelife.loc.address.domain.Address;
import me.freelife.loc.address.domain.ApiType;
import me.freelife.loc.commons.ApiTypeName;
import me.freelife.loc.external.kakao.address.domain.KakaoResKeyword;
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
                ResponseEntity<KakaoResKeyword> data = (ResponseEntity<KakaoResKeyword>) resData;

                KakaoResKeyword.Meta meta = data.getBody().getMeta();
                ArrayList<KakaoResKeyword.Documents> doc = data.getBody().getDocuments();
                doc.forEach(i ->
                    list.add(
                            Address.AddressDocuments.builder()
                                    .addressName(i.getAddressName())
                                    .roadAddressName(i.getRoadAddressName())
                                    .longitude(i.getLongitude())
                                    .latitude(i.getLatitude())
                                    .distance(i.getDistance())
                                    .placeName(i.getPlaceName())
                                    .categoryName(i.getCategoryName())
                                    .phone(i.getPhone())
                                    .placeUrl(i.getPlaceUrl())
                                    .build()
                    )
                );

                address = address.builder()
                        .type(ApiType.KAKAO)
                        .totalCount(meta.getTotalCount())
                        .pageableCount(meta.getPageableCount())
                        .isEnd(meta.isEnd())
                        .address(list).build();
            break;
            case ApiTypeName.NAVER:


                break;
        }

        return address;
    }
}
