let keyword;
let addressList;
$(function() {
    $('[data-toggle="offcanvas"]').click(function () {
        $('.row-offcanvas').toggleClass('active')
    });

    /**
     * 최초 로딩 시 키워드 순위 TOP 10 빈 껍데기 생성
     */
    // onclick="getTopKeyword('top${el}')
    $('.keyword-table-body tr').remove();
    Array.from(Array(10).keys()).forEach(el => {
        $('.keyword-table-body').append(
            `<tr id="top${el}" data-word="" ">
                <td class="keywordId">${el+1}</td>
                <td class="word"></td>
                <td class="searchCount"></td>
            </tr>`
        )
    });

    /**
     * 최초 로딩 시 키워드 순위 TOP 10 데이터 조회해서 테이블에 맵핑
     */
    $.getJSON("/api/keywordList")
        .done(res => {
            if(res.length > 0) {
                res.forEach((el, i) => {
                    $('#top'+i).attr('data-word',el.word);
                    $('.word').eq(i).text(el.word);
                    $('.searchCount').eq(i).text(el.searchCount);
                })
            }
        });

});

/**
 * 키워드 주소 검색
 * @param page
 */
const getAddress = (page) => {
    let size = 5;
    let jsonData = {keyword:keyword, page:page, size:size, type:'KAKAO' }
    $.getJSON("/api/searchKeyword", jsonData)
        .done(res => {
            addressList = res.address.address;
            makeAddressTable(addressList);
            if(res.address.pageableCount == 0)
                $('#pagination ul').remove();
            else
                makePagination(res, jsonData);
            getKeywordList(res);

        })
        .fail(function( jqxhr, textStatus, error ) {
            alert('시스템 오류');
        });
    ;

}

/**
 * 키워드 검색 순위 TOP 10
 * 최초 로딩 시에만 가져오고 다음부터는 키워드 주소 검색에서 같이 가져옴
 * @param res
 */
const getKeywordList = (res) => {
    $('.word').text('');
    $('.searchCount').text('');
    res.keywordList.forEach((el, i) => {
        $('#top'+i).attr('data-word',el.word);
        $('.word').eq(i).text(el.word);
        $('.searchCount').eq(i).text(el.searchCount);
    })
}

/**
 * 키워드 주소 검색 테이블 생성
 * @param address
 */
const makeAddressTable = (address) => {
    let list = address;
    $('#addressList tr').remove();
    let table = $('#addressList');
    if(list.length > 0) {
        list.forEach((el, i) => {
            table.append(
                `<tr id="address${i}" onclick="drawMapAddress(${i})" role="row">
                <td class="roadAddressName">${el.roadAddressName}</td>
                <td class="placeName">${el.placeName}</td>
                <td class="phone">${el.phone}</td>
                <td class="latitude">${el.latitude}</td>
                <td class="longitude">${el.longitude}</td>
            </tr>`
            );
        });
    }else {
        table.append(
            `<tr role="row">
                <td rowspan="5" colspan="5" class="text-center">조회된 검색 결과가 없습니다</td>
            </tr>
            `
        );
    }
}

let startPage = 0;
let endPage = 0;
/**
 * 페이지네이션 생성
 * @param res
 * @param jsonData
 */
const makePagination = (res, jsonData) => {
    let page = jsonData.page;
    let size = jsonData.size;
    let totalPages = Math.ceil(res.address.pageableCount / size);
    // console.log("pageableCount = ",res.address.pageableCount,", totalPages = ",totalPages);

    let pageButtonRange = 5;
    let pageRange = new Array();

    if(totalPages < pageButtonRange){
        pageRange = Array.from(new Array(totalPages),(val,index)=>index+1);
    }else{
        if(page < pageButtonRange){
            pageRange = Array.from(new Array(pageButtonRange),(val,index)=>index+1);
        } else if(page % pageButtonRange == 0) {
            startPage = page;
            endPage = totalPages - page > pageButtonRange ? page + (page + pageButtonRange-1) : totalPages
            pageRange = Array.from(new Array((endPage-startPage)+1), (val, index) => index + startPage);
        } else{
            pageRange = Array.from(new Array((endPage-startPage)+1), (val, index) => index + startPage);
        }
    }

    $('#total-data').text(`총 데이터: ${res.address.pageableCount}`)
    $('#page-count').text(`, 페이지 ${page} / ${totalPages}`)


    // Array.from(new Array(size),(val,index)=>index+page);

    $('#pagination ul').remove();
    $('#pagination').append(
        `<ul class="pagination">
            <li><a aria-label="Previous" href="#" onclick="getAddress(1);">&laquo;</a></li>
                <li class="first" style="${page == 1 ? 'display:none' : ''}">
                    <a href="#" onclick="getAddress(${page-1})">&lsaquo;</a>
                </li>
        </ul>`
    )
    pageRange.forEach((el) => {
        $('#pagination ul').append(
            `<li class="${el == page ? 'active' : ''}">
                <a href="#" onclick="getAddress(${el})">${el}<span class="sr-only"></span></a>
            </li>`
        )
    });

    $('#pagination ul').append(
        `<li class="last" style="${res.address.end ? 'display:none' : ''}">
            <a href="#" onclick="getAddress(${page+1})" }">&rsaquo;</a>
        </li>
        <li><a aria-label="Next" href="#" onclick="getAddress(${totalPages})">&raquo;</a></li>
        `
    )
}


/**
 * 키워드 검색 요청 공통 함수
 */
const reqData = () => {
    keyword = $('#keyword').val();
    if(keyword.trim() == ''){
        alert('입력된 키워드가 없습니다 키워드를 입력해주세요');
        $('#keyword').focus();
        return;
    }
    getAddress(1);
    $('#keyword').val('');
}

/**
 * postJSON 처리
 * @param url
 * @param data
 * @param callback
 * @returns {*|boolean|{readyState, getResponseHeader, getAllResponseHeaders, setRequestHeader, overrideMimeType, statusCode, abort}}
 */
jQuery["postJSON"] = function( url, data, callback ) {
    // shift arguments if data argument was omitted
    if ( jQuery.isFunction( data ) ) {
        callback = data;
        data = undefined;
    }

    return jQuery.ajax({
        url: url,
        type: "POST",
        contentType:"application/json; charset=utf-8",
        dataType: "json",
        data: data,
        success: callback,
        error: function () {
            alert('시스템 오류');
        }
    });
};

/**
 * putJSON 처리
 * @param url
 * @param data
 * @param callback
 * @returns {*|boolean|{readyState, getResponseHeader, getAllResponseHeaders, setRequestHeader, overrideMimeType, statusCode, abort}}
 */
jQuery["putJSON"] = function( url, data, callback ) {
    // shift arguments if data argument was omitted
    if ( jQuery.isFunction( data ) ) {
        callback = data;
        data = undefined;
    }

    return jQuery.ajax({
        url: url,
        type: "PUT",
        contentType:"application/json; charset=utf-8",
        dataType: "json",
        data: data,
        success: callback,
        error: function( jqxhr, textStatus, error ) {
            console.log(jqxhr,textStatus,error)
        }
    });
};

/**
 * 엔터키와 검색 버튼 클릭에 따라 키워드 검색 요청
 */
$(() => {
    /**
     * 엔터키 이벤트
     */
    $('#keyword').keydown(function (e) {
        if(e.which === 13){
            reqData();
        }
    })

    /**
     * 검색 버튼 클릭 이벤트
     */
    $('#searchBtn').click(function (e) {
        reqData();
    })


    /**
     * API 설정 변경 처리
     */
    $('input[name=apiType]').click(function (e) {
        if($(this).val()) {

            let dataString = JSON.stringify({apiType: $(this).val()});

            $.putJSON( '/api/apiType', dataString  )
                .done(function( res ) {
                },"json");
        }
    })

    /**
     * 키워드 순위 TOP 10 클릭시 해당 키워드로 조회
     */
    $('.keyword-table-body tr[id^=top]').click(function (e) {
        let topKeyword = $(this).attr('data-word');
        if(topKeyword){
            keyword = topKeyword;
            getAddress(1)
        }
    })
});


/********************************************
 *                다음 지도 설정
 ********************************************/

let mapContainer = document.getElementById('map'); // 지도의 중심좌표
let mapOption = {
    center: new daum.maps.LatLng(33.451475, 126.570528), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
};
let map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
// 지도에 마커를 표시합니다
let marker = new daum.maps.Marker({
    map: map,
    position: new daum.maps.LatLng(33.450701, 126.570667)
});

// 커스텀 오버레이에 표시할 컨텐츠 입니다
// 커스텀 오버레이는 아래와 같이 사용자가 자유롭게 컨텐츠를 구성하고 이벤트를 제어할 수 있기 때문에
// 별도의 이벤트 메소드를 제공하지 않습니다
let content;

// 마커 위에 커스텀오버레이를 표시합니다
// 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS를 이용해 위치를 설정했습니다
let overlay;


// 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
const closeOverlay =() => {
    overlay.setMap(null);
}

const drawMapAddress = (index) => {

    let data = addressList[index];
    mapOption = {
        center: new daum.maps.LatLng(data.latitude, data.longitude), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };
    map = new daum.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 지도에 마커를 표시합니다
    marker = new daum.maps.Marker({
        map: map,
        position: new daum.maps.LatLng(data.latitude, data.longitude)
    });

    // 커스텀 오버레이에 표시할 컨텐츠 입니다
    // 커스텀 오버레이는 아래와 같이 사용자가 자유롭게 컨텐츠를 구성하고 이벤트를 제어할 수 있기 때문에
    // 별도의 이벤트 메소드를 제공하지 않습니다
    content = `<div class="wrap">
                       <div class="info">
                           <div class="title">
                               ${data.placeName}
                               <div class="close" onclick="closeOverlay()" title="닫기"></div>
                           </div>
                           <div class="body">
                               <!--<div class="img">-->
                                   <!--<img src="http://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">-->
                              <!--</div>-->
                               <div class="desc">
                                   <div class="ellipsis">${data.roadAddressName}</div>
                                   <div class="jibun ellipsis">(지번) ${data.addressName}</div>`
                                   + (data.phone ? `<div class="jibun ellipsis">(전화번호) ${data.phone}</div>` : ``)+
                                   `<div><a href="${data.placeUrl}" target="_blank" class="link">장소URL</a></div>
                               </div>
                           </div>
                       </div>
                   </div>`;

    // 마커 위에 커스텀오버레이를 표시합니다
    // 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS를 이용해 위치를 설정했습니다
    overlay = new daum.maps.CustomOverlay({
        content: content,
        map: map,
        position: marker.getPosition()
    });


    // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
    daum.maps.event.addListener(marker, 'click', function() {
        overlay.setMap(map);
    });


}
