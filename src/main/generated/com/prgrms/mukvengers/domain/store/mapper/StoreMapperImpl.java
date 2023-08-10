package com.prgrms.mukvengers.domain.store.mapper;

import com.prgrms.mukvengers.domain.store.dto.request.CreateStoreRequest;
import com.prgrms.mukvengers.domain.store.dto.response.StoreResponse;
import com.prgrms.mukvengers.domain.store.model.Store;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class StoreMapperImpl implements StoreMapper {

    @Override
    public Store toStore(CreateStoreRequest createStoreRequest) {
        if ( createStoreRequest == null ) {
            return null;
        }

        Store.StoreBuilder store = Store.builder();

        store.location( mapPoint( createStoreRequest ) );
        store.placeId( createStoreRequest.placeId() );
        store.placeName( createStoreRequest.placeName() );
        store.categories( createStoreRequest.categories() );
        store.roadAddressName( createStoreRequest.roadAddressName() );
        store.photoUrls( createStoreRequest.photoUrls() );
        store.kakaoPlaceUrl( createStoreRequest.kakaoPlaceUrl() );
        store.phoneNumber( createStoreRequest.phoneNumber() );

        return store.build();
    }

    @Override
    public StoreResponse toStoreResponse(Store store) {
        if ( store == null ) {
            return null;
        }

        Double longitude = null;
        Double latitude = null;
        Long id = null;
        String placeId = null;
        String placeName = null;
        String categories = null;
        String roadAddressName = null;
        String photoUrls = null;
        String kakaoPlaceUrl = null;
        String phoneNumber = null;

        longitude = mapLongitude( store );
        latitude = mapLatitude( store );
        id = store.getId();
        placeId = store.getPlaceId();
        placeName = store.getPlaceName();
        categories = store.getCategories();
        roadAddressName = store.getRoadAddressName();
        photoUrls = store.getPhotoUrls();
        kakaoPlaceUrl = store.getKakaoPlaceUrl();
        phoneNumber = store.getPhoneNumber();

        StoreResponse storeResponse = new StoreResponse( id, longitude, latitude, placeId, placeName, categories, roadAddressName, photoUrls, kakaoPlaceUrl, phoneNumber );

        return storeResponse;
    }
}
