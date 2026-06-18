package com.canteen.bc.canteen_system.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallets")
@Getter // 💡 修正：拆開用 Getter / Setter，唔好用 @Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
    
    // 💡 手動重寫 toString，排除掉 user，避免 StackOverflow
    @Override
    public String toString() {
        return "WalletEntity{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}