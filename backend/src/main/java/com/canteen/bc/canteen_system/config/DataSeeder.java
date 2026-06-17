package com.canteen.bc.canteen_system.config;

import com.canteen.bc.canteen_system.entity.*;
import com.canteen.bc.canteen_system.model.DataType;
import com.canteen.bc.canteen_system.model.Role;
import com.canteen.bc.canteen_system.model.UserType;
import com.canteen.bc.canteen_system.repository.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private WalletRepository walletRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private SysConfigRepository sysConfigRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Hong Kong local restaurant dishes — verified Wikimedia Commons photos (250px/330px thumbnails)
    private static final Map<String, String> ITEM_IMAGES = Map.of(
        // Bowl of wonton noodle soup served in a HK shop (Queen's Road Central)
        "Wonton Noodles",  "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/HK_SW_%E4%B8%8A%E7%92%B0_Sheung_Wan_%E7%9A%87%E5%90%8E%E5%A4%A7%E9%81%93%E4%B8%AD_303_Queen%27s_Road_Central_%E6%AC%8A%E8%A8%98%E9%9B%B2%E5%90%9E%E9%BA%B5_Wonton_noodle_soup_shop_June_2020_SS2_10.jpg/250px-HK_SW_%E4%B8%8A%E7%92%B0_Sheung_Wan_%E7%9A%87%E5%90%8E%E5%A4%A7%E9%81%93%E4%B8%AD_303_Queen%27s_Road_Central_%E6%AC%8A%E8%A8%98%E9%9B%B2%E5%90%9E%E9%BA%B5_Wonton_noodle_soup_shop_June_2020_SS2_10.jpg",
        // Actual char siu rice plate from HKU canteen — BBQ pork on rice with greens
        "Char Siu Rice",   "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a6/HKU_%E9%A6%99%E6%B8%AF%E5%A4%A7%E5%AD%B8_Pok_Fu_Lam_Road_campus_%E8%8E%8A%E6%9C%88%E6%98%8E%E6%96%87%E5%A8%9B%E4%B8%AD%E5%BF%83_Chong_Yuet_Ming_Building_%E9%A4%90%E5%BB%B3_Canteen_%E5%8D%88%E9%A4%90_Lunch_%E7%87%92%E5%91%B3_siu_mei_%E5%8F%89%E7%87%92%E9%A3%AF_Char_Siu_Rice_February_2023_Px3_02.jpg/330px-thumbnail.jpg",
        // Char siu cheung fun — rice noodle roll with BBQ pork at a Cantonese restaurant
        "Cheung Fun",      "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Cha_siu_choeng.jpg/250px-Cha_siu_choeng.jpg",
        // HK-style egg tarts (Macau variant) — golden custard in flaky pastry
        "Egg Tart",        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Macau_Eggtart.jpg/330px-Macau_Eggtart.jpg",
        // Cantonese siu mai in bamboo steamer basket — yellow open dumplings
        "Siu Mai",         "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Yellow_dim_sum_in_steamer_basket.jpg/250px-Yellow_dim_sum_in_steamer_basket.jpg",
        // HK-style milk tea in classic Black & White evaporated milk cup
        "HK Milk Tea",     "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Hong_Kong_Milk_Tea%2C_Cha_Chaan_Teng-style_with_Black_and_White_cup.jpg/250px-Hong_Kong_Milk_Tea%2C_Cha_Chaan_Teng-style_with_Black_and_White_cup.jpg",
        // Pineapple buns — golden crispy-top sweet buns
        "Pineapple Bun",   "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Pineapple_buns.jpg/250px-Pineapple_buns.jpg",
        // Chinese rice congee — smooth Cantonese porridge
        "Sampan Congee",   "https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Chinese_rice_congee.jpg/250px-Chinese_rice_congee.jpg",
        // Authentic HK-style French toast — deep fried, with syrup and butter
        "HK French Toast", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/HKStyleFrenchtoast.jpg/250px-HKStyleFrenchtoast.jpg",
        // Close-up of egg waffle (雞蛋仔) showing the bubble structure
        "Egg Waffle",      "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c6/Egg_Waffle_again.JPG/250px-Egg_Waffle_again.JPG"
    );

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        seedItems();
        seedSysConfig();
    }

    private void seedUsers() {
        if (userRepository.existsBySchoolId("ADMIN001")) return;

        UserEntity admin = userRepository.save(UserEntity.builder()
                .schoolId("ADMIN001").name("Canteen Admin")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN).userType(UserType.STAFF).build());

        UserEntity stu1 = userRepository.save(UserEntity.builder()
                .schoolId("STU001").name("Chan Siu Ming")
                .password(passwordEncoder.encode("student123"))
                .role(Role.CUSTOMER).userType(UserType.STUDENT).build());

        UserEntity stu2 = userRepository.save(UserEntity.builder()
                .schoolId("STU002").name("Wong Mei Ling")
                .password(passwordEncoder.encode("student123"))
                .role(Role.CUSTOMER).userType(UserType.STUDENT).build());

        UserEntity staff1 = userRepository.save(UserEntity.builder()
                .schoolId("STAFF001").name("Mr. Lam Wai Kit")
                .password(passwordEncoder.encode("staff123"))
                .role(Role.CUSTOMER).userType(UserType.STAFF).build());

        UserEntity kitchen1 = userRepository.save(UserEntity.builder()
                .schoolId("KITCHEN001").name("Auntie Fong")
                .password(passwordEncoder.encode("kitchen123"))
                .role(Role.KITCHEN).userType(UserType.STAFF).build());

        walletRepository.save(WalletEntity.builder().user(admin).balance(new BigDecimal("500.00")).build());
        walletRepository.save(WalletEntity.builder().user(stu1).balance(new BigDecimal("120.00")).build());
        walletRepository.save(WalletEntity.builder().user(stu2).balance(new BigDecimal("55.00")).build());
        walletRepository.save(WalletEntity.builder().user(staff1).balance(new BigDecimal("200.00")).build());
        walletRepository.save(WalletEntity.builder().user(kitchen1).balance(new BigDecimal("0.00")).build());

        System.out.println("[DataSeeder] Users seeded — ADMIN001/admin123, STU001/student123, STU002/student123, STAFF001/staff123, KITCHEN001/kitchen123");
    }

    private void seedItems() {
        if (itemRepository.count() > 0) {
            // Refresh image URLs in case they changed
            ITEM_IMAGES.forEach((name, url) ->
                itemRepository.findByName(name).ifPresent(item -> {
                    item.setImageUrl(url);
                    itemRepository.save(item);
                })
            );
            return;
        }

        // ── Hong Kong Cha Chaan Teng dishes ──────────────────────────────────
        ItemEntity wontonNoodles = itemRepository.save(ItemEntity.builder()
                .name("Wonton Noodles")
                .description("雲吞麵 — Thin egg noodles in clear shrimp broth with handmade prawn wontons")
                .price(new BigDecimal("38.00")).imageUrl(ITEM_IMAGES.get("Wonton Noodles")).isVisible(true).build());

        ItemEntity charSiuRice = itemRepository.save(ItemEntity.builder()
                .name("Char Siu Rice")
                .description("叉燒飯 — Cantonese BBQ pork over steamed jasmine rice with seasonal greens")
                .price(new BigDecimal("42.00")).imageUrl(ITEM_IMAGES.get("Char Siu Rice")).isVisible(true).build());

        ItemEntity cheungFun = itemRepository.save(ItemEntity.builder()
                .name("Cheung Fun")
                .description("腸粉 — Silky steamed rice noodle rolls with dried shrimp and spring onion, drizzled with sweet soy")
                .price(new BigDecimal("28.00")).imageUrl(ITEM_IMAGES.get("Cheung Fun")).isVisible(true).build());

        ItemEntity eggTart = itemRepository.save(ItemEntity.builder()
                .name("Egg Tart")
                .description("蛋撻 — Classic Hong Kong-style flaky pastry shell with smooth egg custard filling, freshly baked")
                .price(new BigDecimal("8.00")).imageUrl(ITEM_IMAGES.get("Egg Tart")).isVisible(true).build());

        ItemEntity siuMai = itemRepository.save(ItemEntity.builder()
                .name("Siu Mai")
                .description("燒賣 — Steamed open-top dim sum dumplings filled with pork and shrimp (3 pcs)")
                .price(new BigDecimal("32.00")).imageUrl(ITEM_IMAGES.get("Siu Mai")).isVisible(true).build());

        ItemEntity milkTea = itemRepository.save(ItemEntity.builder()
                .name("HK Milk Tea")
                .description("港式奶茶 — Hong Kong-style silky milk tea brewed with Ceylon black tea and evaporated milk, hot or iced")
                .price(new BigDecimal("18.00")).imageUrl(ITEM_IMAGES.get("HK Milk Tea")).isVisible(true).build());

        ItemEntity pineappleBun = itemRepository.save(ItemEntity.builder()
                .name("Pineapple Bun")
                .description("菠蘿油 — Iconic HK sweet bun with crispy sugar crust, served warm with a thick slab of butter")
                .price(new BigDecimal("18.00")).imageUrl(ITEM_IMAGES.get("Pineapple Bun")).isVisible(true).build());

        ItemEntity congee = itemRepository.save(ItemEntity.builder()
                .name("Sampan Congee")
                .description("艇仔粥 — Smooth Cantonese rice porridge with fish fillet, pork, peanuts and spring onion")
                .price(new BigDecimal("35.00")).imageUrl(ITEM_IMAGES.get("Sampan Congee")).isVisible(true).build());

        ItemEntity frenchToast = itemRepository.save(ItemEntity.builder()
                .name("HK French Toast")
                .description("西多士 — Deep-fried thick toast sandwich with peanut butter filling, served with golden syrup and butter")
                .price(new BigDecimal("28.00")).imageUrl(ITEM_IMAGES.get("HK French Toast")).isVisible(true).build());

        ItemEntity eggWaffle = itemRepository.save(ItemEntity.builder()
                .name("Egg Waffle")
                .description("雞蛋仔 — Freshly made crispy-outside, fluffy-inside bubble waffle, a beloved Hong Kong street snack")
                .price(new BigDecimal("22.00")).imageUrl(ITEM_IMAGES.get("Egg Waffle")).isVisible(true).build());

        // ── Menus ────────────────────────────────────────────────────────────
        MenuEntity breakfast = menuRepository.save(MenuEntity.builder()
                .name("Breakfast").isActive(true).build());

        MenuEntity lunch = menuRepository.save(MenuEntity.builder()
                .name("Lunch").isActive(true).build());

        // ── Breakfast: cha chaan teng morning items ───────────────────────────
        for (ItemEntity item : List.of(pineappleBun, frenchToast, congee, cheungFun, milkTea, eggTart)) {
            menuItemRepository.save(MenuItemEntity.builder()
                    .menuEntity(breakfast).itemEntity(item).build());
        }

        // ── Lunch: heavier mains + dim sum + drinks ───────────────────────────
        for (ItemEntity item : List.of(wontonNoodles, charSiuRice, siuMai, cheungFun, eggTart, eggWaffle, milkTea)) {
            menuItemRepository.save(MenuItemEntity.builder()
                    .menuEntity(lunch).itemEntity(item).build());
        }

        System.out.println("[DataSeeder] HK menu items seeded.");
    }

    private void seedSysConfig() {
        if (sysConfigRepository.findByConfigKey("LunchCutOffKey").isEmpty()) {
            sysConfigRepository.save(SysConfigEntity.builder()
                    .configKey("LunchCutOffKey").configValue("11:30:00")
                    .dataType(DataType.TIME).description("Daily lunch order cut-off time").build());
        }
        if (sysConfigRepository.findByConfigKey("OrderWindowOpen").isEmpty()) {
            sysConfigRepository.save(SysConfigEntity.builder()
                    .configKey("OrderWindowOpen").configValue("07:30:00")
                    .dataType(DataType.TIME).description("Ordering window open time").build());
        }
        if (sysConfigRepository.findByConfigKey("OrderWindowClose").isEmpty()) {
            sysConfigRepository.save(SysConfigEntity.builder()
                    .configKey("OrderWindowClose").configValue("14:30:00")
                    .dataType(DataType.TIME).description("Ordering window close time").build());
        }
    }
}
