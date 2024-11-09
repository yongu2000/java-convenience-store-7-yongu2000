package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.order.Choice;

public class InputView {
    private static final String ORDER = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String ADD_PROMOTION_CHOICE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String REMOVE_NON_PROMOTION_CHOICE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP_DISCOUNT_CHOICE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String SHOP_AGAIN_CHOICE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String readOrder() {
        System.out.println(ORDER);
        return Console.readLine();
    }

    public Choice readAddPromotionChoice() {
        System.out.println(ADD_PROMOTION_CHOICE);
        return Choice.ofString(Console.readLine());
    }

    public Choice readRemovePromotionChoice() {
        System.out.println(REMOVE_NON_PROMOTION_CHOICE);
        return Choice.ofString(Console.readLine());
    }

    public Choice readMembershipDiscountChoice() {
        System.out.println(MEMBERSHIP_DISCOUNT_CHOICE);
        return Choice.ofString(Console.readLine());
    }

    public Choice readShopAgainChoice() {
        System.out.println(SHOP_AGAIN_CHOICE);
        return Choice.ofString(Console.readLine());
    }
}