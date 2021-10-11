package com.jungho.coding_test.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch=FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, CAMP

    //==생성 메서드==//
    public static Delivery createDelivery(Address address, DeliveryStatus status) {
        Delivery delivery = new Delivery();

        delivery.setAddress(address);
        delivery.setStatus(status);

        return delivery;
    }

}
