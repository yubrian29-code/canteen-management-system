# canteen-management-system

Functional Specifications Proposal: Canteen Management System (MVP)
1. Introduction & Core Design Principles
Security and system interactions are governed strictly by two primary user roles:

Customer Authority: App-wide access for both Students and School Staff (Registration requiring student/staff ID to avoid duplicate accounts).
Admin Authority: Management and fulfilment access for Canteen/Restaurant Staff.
2. The Customer Experience Matrix (Students & School Staff)
Customers access a unified web portal to browse menus, manage personal balances, execute checkouts, track live order progress and order & spending history.

2.1 Interface & Profile Access
Unified Layout: Customers access the system via any standard mobile or desktop web browser with an identical user interface layout.
Account Dashboard: Displays the customer's profile information and their active internal virtual wallet balance.
2.2 Menu Browsing & Selection
Lunch Menu View: Displays the active lunch menu sets available for the current business day. The number of meal sets displayed is dynamically constrained by the Admin.
Visual Elements: Every menu item features exactly one high-quality photograph alongside its price, description, and real-time availability status.
Menu Filtering: Customers view a singular, unified list of items; there is no staff-exclusive food or premium pricing visibility in this phase.
2.3 Ordering Horizons & Time Windows
Instant Ordering: Customers can place orders for immediate preparation during active kitchen operating hours.
Pre-Ordering: Customers can place orders in advance for the lunch rush, provided the checkout is completed prior to the strict lunch order cut-off time configured by the Admin.
2.4 Hybrid Payment Engine
Upon checkout, a customer must select one of two valid payment methods:

Internal Virtual Wallet: The system immediately debits the order total from the customer's pre-loaded app balance. Wallet top-ups are handled via external electronic methods (Credit Card, FPS, etc.).
Cash on Pickup: The order is processed immediately as an "unpaid" transaction. The customer settles the balance using physical cash or Octopus directly at the counter upon food collection. (regarding limit on order items for cash payment - see notes)
2.5 Live Tracking & Cancellation Loop
In-App Monitor: After checkout, the customer is directed to an in-app tracking route displaying a unique order number and a live-updating state label.
Self-Service Cancellation: Customers are permitted to cancel an order directly within the app, provided the action occurs before the designated admin cut-off time.
Counter Refunds: For cancelled wallet transactions, refunds are processed strictly via physical cash at the canteen counter to maintain a lean MVP architecture.
3. The Admin Experience Matrix (Canteen & Kitchen Staff)
Canteen administrators and kitchen operators utilise management features to control menus, update order statuses, configure time rules, and pull operational metrics.

3.1 Real-Time Menu Management
Dynamic Menu Modification: Admins can add, edit, or modify menu item descriptions, photos, and prices at any point during the business day with immediate frontend replication.
Display Configuration: High-priority feature allowing the admin to set and restrict the exact number of primary lunch meal sets displayed to customers.
3.2 Operating Window Control
Cut-Off Configuration: Admins can define and modify the exact daily deadline time for lunch orders, which automatically controls customer pre-ordering and cancellation permissions.
3.3 Kitchen Fulfilment Dashboard
Quick-Tap Kanban Board: Kitchen operators utilise a simplified status board to advance orders through the preparation pipeline via rapid-tap interaction buttons.
Preventative Inventory Buffer: When a lunch set runs low, kitchen staff manually hide the item from the customer menu via a single toggle, preventing system race conditions during a rush.
3.4 End-of-Day Operations & Automated Sweeps
Unclaimed Food Sweep: At the close of the business day, any order remaining in a uncollected state is automatically transitioned by the server to an inactive state to clear the live pickup queue. Admin can also manually move the uncollected order from the ready for pick up status to uncollected as they see fit.
3.5 Automated Metrics Logging
The system background-logs critical operational timestamps for every transaction to support future analytical reporting:

order_time: The exact millisecond the transaction is created.
ready_for_pickup_time: The exact millisecond the kitchen staff flag the order as prepared.
Other statistics to log including: sales, top-selling items, order abandon rate (user linked) etc.
4. The Order State Machine Matrix
Every transaction created in the Java Spring Boot backend must step through this definitive lifecycle matrix, directly updating the React customer tracking interface:

Initial State	Target State	Trigger Event	Operational Condition
None	ORDERED	Customer completes app checkout.	Marked as Paid (Wallet) or Unpaid (Cash selection). Logs order_time.
ORDERED	PREPARING	Automatic after the order is confirmed.	Moves the order ticket into the active preparation queue.
PREPARING	READY FOR PICK UP	Kitchen operator taps "Prepared" button on dashboard.	Triggers system timestamp log for ready_for_pickup_time to track production speed.
READY FOR PICK UP	PICKED UP	Counter staff hand over food to customer.	Cash balances must be collected if marked Unpaid.
ORDERED / PREPARING	CANCELLED	Customer clicks "Cancel" button in tracking portal.	Valid only before the admin-defined cut-off time.
READY FOR PICK UP	ABANDONED	Automated end-of-day system sweep executes / Admin override	Order was never claimed by the close of business.
5. System Roadmap: MVP vs. Future Phases
🟩 MVP Phase (Current Scope)
Unified, fully responsive customer and admin web app portals.
Live item and price editing capabilities.
Configurable lunch menu display limits and cut-off timers.
Hybrid wallet top-up and cash-at-pickup checkout logic.
Manual kitchen buffer toggles to hide low-stock food sets.
Basic timestamp tracking (order_time and ready_for_pickup_time).
🟨 Future Phases (Explicitly Out of Scope)
Automated Inventory Tracking: Full database inventory deduction that automatically triggers a system lockdown when an item reaches absolute zero.
Daily Supply Constraints: Strict item-quantity hard caps (e.g., stopping orders automatically when Meal A hits exactly 100 sales).
Granular Category Tab Customisation: Creating separate, dynamic menu tabs for secondary items such as drinks, snacks, and desserts.
External Notification Gateways: Push Notifications, SMS alerts, or Email receipts.
Feedback & Social Functions: Allowing users to submit feedback and share orders on social media