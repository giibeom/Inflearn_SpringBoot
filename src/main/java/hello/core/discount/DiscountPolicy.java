package hello.core.discount;

import hello.core.member.Member;

/**
 * @author Created by 명기범 on 2021-12-01
 */
public interface DiscountPolicy {

    /**
     * @return 할인 대상 금액
     */
    int discount(Member member, int price);

}
