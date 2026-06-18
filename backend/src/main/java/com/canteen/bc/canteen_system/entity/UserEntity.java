package com.canteen.bc.canteen_system.entity;

import com.canteen.bc.canteen_system.model.Role;
import com.canteen.bc.canteen_system.model.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter // 建議直接放喺類別級別，除非有欄位想 Read-only
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "school_id", nullable = false, unique = true, length = 50)
  private String schoolId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  // 密碼長度建議留長啲（例如 255），因為 Bcrypt / Argon2 加密後嘅 Hash 幾長吓
  @Column(name = "password", nullable = false, length = 255) 
  private String password;

  @Column(name = "role", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "user_type", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private UserType userType;

  // 💡 修正：移除 Setter，改用下面的 setWallet 輔助方法
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private WalletEntity wallet;

  /**
   * 💡 雙向關聯輔助方法
   * 咁樣做可以確保你 user.setWallet(wallet) 的時候，wallet 內部都會自動 binding 返呢個 user
   */
  public void setWallet(WalletEntity wallet) {
      if (wallet == null) {
          if (this.wallet != null) {
              this.wallet.setUser(null);
          }
      } else {
          wallet.setUser(this);
      }
      this.wallet = wallet;
  }
}