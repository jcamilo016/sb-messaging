package com.eafit.sac.services.messaging.entity;

import com.eafit.sac.services.messaging.utils.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tblmessage")
@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmessage")
    private Long id;

    @Column(name = "sentdate")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date sentDate;

    @Column(name="sentto")
    private String sendTo;

    @Column(name = "sentstatus")
    @Enumerated(EnumType.STRING)
    private MessageStatus sentStatus;

    @Column(name = "messagedetail")
    private String message;

}
