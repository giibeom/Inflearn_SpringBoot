package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

/**
 * @author Created by 명기범 on 2021-12-01
 */
public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000; // 1000원 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
