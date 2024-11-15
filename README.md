# 편의점

## 기능 요구 사항

구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.

- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.
    - 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
- 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
- Exception이 아닌 IllegalArgumentException, IllegalStateException 등과 같은 명확한 유형을 처리한다.

##### 재고 관리

- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.

##### 프로모션 할인

- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
- 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
- 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
- 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.

##### 멤버십 할인

- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
- 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- 멤버십 할인의 최대 한도는 8,000원이다.

##### 영수증 출력

- 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
- 영수증 항목은 아래와 같다.
    - 구매 상품 내역: 구매한 상품명, 수량, 가격
    - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - 금액 정보
        - 총구매액: 구매한 상품의 총 수량과 총 금액
        - 행사할인: 프로모션에 의해 할인된 금액
        - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - 내실돈: 최종 결제 금액
- 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.

---

## 프로그램 흐름

1. 편의점이 보유하고 있는 상품 목록 출력
2. 고객이 구입할 상품과 수량 입력받기
3. 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받기
4. 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받기
5. 멤버십 할인 적용 여부를 입력 받기
6. 추가 구매 여부를 입력 받기
    1. 추가 구매 Y시, 1로 돌아가기
    2. N시, 프로그램 종료

---

## 프로그램 흐름 세부적으로 나누기

1. 편의점 초기 세팅
    1. 리소스 파일에서 편의점 재고 및 프로모션 적용하기
2. 편의점이 보유하고 있는 상품 목록 출력
    1. 편의점이 보유하고 있는 상품 목록 확인
    2. 편의점이 보유하고 있는 상품 목록 출력
3. 고객이 구입할 상품과 수량 입력받기
    1. 고객이 구입할 상품과 수량 입력
    2. 잘못된 값을 입력하지 않았는지 확인
        - 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
        - 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
        - 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
        - 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    3. 프로모션 상품들 먼저 프로모션 구입 목록에 담기
    4. 프로모션 상품이 남았을 경우 프로모션 적용 가능한 상품이 무엇, 몇개인지 확인.
        1. 존재할 경우, 무엇, 몇개인지 4번에 전달.
        2. 존재하지 않을 경우 4번 건너뛰기.
    5. 프로모션 재고 부족한 상품이 무엇, 몇개인지 확인.
        1. 존재할 경우, 무엇, 몇개인지 5번에 전달.
        2. 존재하지 않을 경우 5번 건너뛰기.
    6. 비 프로모션 상품들 구입 목록에 담기
4. 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받기
    1. 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    2. Y 선택시, 3-3에서 전달받은 상품, 수량만큼 프로모션 구입 목록에 추가
5. 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받기
    1. 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    2. N 선택시, 3-4에서 전달받은 상품, 수량만큼 구입 목록에서 제거
6. 멤버십 할인 적용 여부를 입력 받기
    1. 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    2. Y 선택시, 비프로모션 상품들 목록의 30%만큼 할인하기
7. 영수증 출력하기
    - 총구매액: 구매한 상품의 총 수량과 총 금액
    - 행사할인: 프로모션에 의해 할인된 금액
    - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    - 내실돈: 최종 결제 금액
8. 추가 구매 여부를 입력 받기
    1. 추가 구매 Y시, 2로 돌아가기
    2. N시, 프로그램 종료

### 주요 도메인 설계

Order ```고객의 주문 관련 상태 및 행위```

Product ```고객의 주문, 편의점 재고 관련 행위 및 상태```

Promotion ```Product의 멤버변수```

ConvenienceStore ```편의점 관련 상태 및 행위```

Receipt ```영수증 관련 상태 및 행위```

## 변경될 수 있는 부분들

편의점 재고 불러오는 방식
멤버십할인 정책

해당 요소들은 이후 변경을 고려하여 인터페이스를 이용.

재고 불러오는 방식이 사용자 입력으로 바뀔수도 있고,
멤버십 할인 정책이 30%가 아닌 -2000원 식의 고정 할인 정책으로 바뀔 수 있음.

## 진행 상황

1. 편의점 초기 세팅
    1. [x] 리소스 파일에서 편의점 재고 및 프로모션 적용하기
2. 편의점이 보유하고 있는 상품 목록 출력
    1. [x] 편의점이 보유하고 있는 상품 목록 확인
    2. [x] 편의점이 보유하고 있는 상품 목록 출력
3. 고객이 구입할 상품과 수량 입력받기
    1. [x] 고객이 구입할 상품과 수량 입력
    2. [x] 잘못된 값을 입력하지 않았는지 확인
        - [x] 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
        - [x] 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
        - [x] 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
    3. [x] 프로모션 상품들 먼저 프로모션 구입 목록에 담기
    4. [x] 프로모션 상품이 남았을 경우 프로모션 적용 가능한 상품이 무엇, 몇개인지 확인.
        1. [x] 존재할 경우, 무엇, 몇개인지 4번에 전달.
        2. [x] 존재하지 않을 경우 4번 건너뛰기.
    5. [x] 프로모션 재고 부족한 상품이 무엇, 몇개인지 확인.
        1. [x] 존재할 경우, 무엇, 몇개인지 5번에 전달.
        2. [x] 존재하지 않을 경우 5번 건너뛰기.
    6. [x] 비 프로모션 상품들 구입 목록에 담기
4. 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받기
    1. [x] 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    2. [x] Y 선택시, 3-3에서 전달받은 상품, 수량만큼 프로모션 구입 목록에 추가
5. 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받기
    1. [x] 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    2. [x] N 선택시, 3-4에서 전달받은 상품, 수량만큼 구입 목록에서 제거
6. 멤버십 할인 적용 여부를 입력 받기
    1. [x] 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    2. [x] Y 선택시, 비프로모션 상품들 목록의 30%만큼 할인하기
7. [x] 영수증 출력하기
    - [x] 총구매액: 구매한 상품의 총 수량과 총 금액
    - [x] 행사할인: 프로모션에 의해 할인된 금액
    - [x] 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    - [x] 내실돈: 최종 결제 금액
8. 추가 구매 여부를 입력 받기
    1. [x] 추가 구매 Y시, 2로 돌아가기
    2. [x] N시, 프로그램 종료


