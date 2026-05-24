package com.orderapp.supplier.service.impl;

import com.orderapp.supplier.entity.*;
import com.orderapp.supplier.mapper.*;
import com.orderapp.supplier.service.ClientAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ClientAppServiceImpl implements ClientAppService {
    private Long currentUserId(){ return user().getId(); }

    private static final String IMG="https://images.unsplash.com/photo-1618220179428-22790b461013?auto=format&fit=crop&w=800&q=80";
    private final ClientUserMapper users;
    private final ClientCartItemMapper carts;
    private final ClientOrderMapper orders;
    private final ClientOrderItemMapper orderItems;
    private final ClientPaymentMapper payments;
    private final ClientAftersaleMapper clientAftersales;
    private final ClientBrowseHistoryMapper history;
    private final SupplierProductMapper products;
    private final SupplierFulfillmentMapper fulfills;
    private final SupplierInstallationMapper installs;
    private final SupplierAftersaleCaseMapper scases;
    private final SupplierWalletTransactionMapper wallets;
    private final SupplierProductMetricMapper metrics;
    private final SupplierAnalyticsDailyMapper analytics;

    public ClientAppServiceImpl(ClientUserMapper users, ClientCartItemMapper carts, ClientOrderMapper orders, ClientOrderItemMapper orderItems, ClientPaymentMapper payments, ClientAftersaleMapper clientAftersales, ClientBrowseHistoryMapper history, SupplierProductMapper products, SupplierFulfillmentMapper fulfills, SupplierInstallationMapper installs, SupplierAftersaleCaseMapper scases, SupplierWalletTransactionMapper wallets, SupplierProductMetricMapper metrics, SupplierAnalyticsDailyMapper analytics) {
        this.users=users; this.carts=carts; this.orders=orders; this.orderItems=orderItems; this.payments=payments; this.clientAftersales=clientAftersales; this.history=history; this.products=products; this.fulfills=fulfills; this.installs=installs; this.scases=scases; this.wallets=wallets; this.metrics=metrics; this.analytics=analytics;
    }

    public Map<String,Object> home(){
        return m("banners",List.of(m("id",1,"title","?????","image",IMG)),"categories",products.selectClientCategories(),"recommend_products",products.selectClientProducts(null,null,null,null,null,null,null,null,0,6).stream().map(this::pm).toList(),"hot_products",products.selectClientHotProducts(6).stream().map(this::pm).toList(),"order_reminders",m("pending_payment",orders.countClientOrders(currentUserId(),"pending_payment"),"pending_delivery",orders.countClientOrders(currentUserId(),"pending_delivery"),"pending_installation",orders.countClientOrders(currentUserId(),"pending_installation"),"aftersale_processing",clientAftersales.selectByCondition(qAftersale("pending")).size()));
    }

    public Map<String,Object> products(Integer page,Integer pageSize,String keyword,Long categoryId,String category,BigDecimal minPrice,BigDecimal maxPrice,String material,String style,Boolean supportInstallation){
        int p=page(page), s=size(pageSize);
        return m("list",products.selectClientProducts(keyword,categoryId,category,minPrice,maxPrice,material,style,supportInstallation,(p-1)*s,s).stream().map(this::pm).toList(),"total",products.countClientProducts(keyword,categoryId,category,minPrice,maxPrice,material,style,supportInstallation),"page",p,"page_size",s);
    }

    public Map<String,Object> productDetail(Long productId){
        SupplierProduct p=product(productId);
        ClientBrowseHistory h=new ClientBrowseHistory(); h.setUserId(user().getId()); h.setSupplierId(p.getSupplierId()); h.setProductId(p.getId()); h.setProductName(p.getName()); h.setProductImage(img(p)); h.setViewedAt(LocalDateTime.now()); h.setCreatedAt(LocalDateTime.now()); history.insert(h);
        Map<String,Object> r=pm(p); r.put("images",List.of(img(p))); r.put("description",p.getDescription()); r.put("delivery_areas",p.getDeliveryAreas()); r.put("package_info",p.getPackageInfo()); return r;
    }

    @Transactional
    public Map<String,Object> addCartItem(Map<String,Object> body){
        Long pid=L(body.get("product_id")); int qty=Math.max(I(body.get("quantity"),1),1); boolean ins=B(body.get("support_installation")); SupplierProduct p=product(pid); ClientCartItem c=carts.selectActiveByUserIdAndProductId(currentUserId(),pid);
        if(c==null){ c=new ClientCartItem(); c.setUserId(currentUserId()); c.setSupplierId(p.getSupplierId()); c.setProductId(pid); c.setQuantity(qty); c.setSupportInstallation(ins); c.setSelected(true); c.setStatus("active"); c.setCreatedAt(LocalDateTime.now()); c.setUpdatedAt(LocalDateTime.now()); carts.insert(c);} else { c.setQuantity(c.getQuantity()+qty); c.setSupportInstallation(ins); c.setUpdatedAt(LocalDateTime.now()); carts.updateById(c); }
        return m("cart_item_id",c.getId(),"product_id",pid,"quantity",c.getQuantity());
    }

    public Map<String,Object> cartItems(){
        List<Map<String,Object>> list=new ArrayList<>(); BigDecimal total=BigDecimal.ZERO;
        for(ClientCartItem c:carts.selectActiveByUserId(currentUserId())){ SupplierProduct p=products.selectClientProductById(c.getProductId()); if(p==null) continue; BigDecimal a=p.getPrice().multiply(BigDecimal.valueOf(c.getQuantity())); total=total.add(a); list.add(m("cart_item_id",c.getId(),"product_id",p.getId(),"supplier_id",p.getSupplierId(),"name",p.getName(),"image",img(p),"price",p.getPrice(),"quantity",c.getQuantity(),"support_installation",c.getSupportInstallation(),"amount",a)); }
        return m("list",list,"total_amount",total,"total_count",list.size());
    }

    @Transactional
    public Map<String,Object> updateCartItem(Long cartItemId, Map<String,Object> body){
        ClientCartItem c=carts.selectById(cartItemId);
        if(c==null || !currentUserId().equals(c.getUserId())) throw new IllegalArgumentException("???????");
        int qty=I(body.get("quantity"), c.getQuantity()==null?1:c.getQuantity());
        if(qty<=0){ carts.deleteById(cartItemId); return m("cart_item_id",cartItemId,"removed",true); }
        c.setQuantity(qty); c.setUpdatedAt(LocalDateTime.now()); carts.updateById(c);
        return m("cart_item_id",cartItemId,"quantity",qty,"removed",false);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Map<String,Object> createOrder(Map<String,Object> body){
        List<Map<String,Object>> req=body==null?new ArrayList<>():(List<Map<String,Object>>)body.getOrDefault("items",new ArrayList<>());
        if(req.isEmpty()) for(ClientCartItem c:carts.selectActiveByUserId(currentUserId())) req.add(m("product_id",c.getProductId(),"quantity",c.getQuantity(),"support_installation",c.getSupportInstallation()));
        if(req.isEmpty()) throw new IllegalArgumentException("Cart is empty");
        SupplierProduct firstProduct=product(L(req.get(0).get("product_id")));
        ClientOrder o=new ClientOrder(); o.setOrderNo(no("CO")); o.setUserId(user().getId()); o.setSupplierId(firstProduct.getSupplierId()); o.setReceiverName(s(body,"receiver_name","Client User")); o.setReceiverMobile(s(body,"receiver_mobile","13800000000")); o.setReceiverAddress(s(body,"receiver_address","Default address")); o.setDeliveryMethod(s(body,"delivery_method","home_delivery")); o.setProductAmount(BigDecimal.ZERO); o.setDeliveryFee(BigDecimal.ZERO); o.setInstallationFee(BigDecimal.ZERO); o.setDiscountAmount(BigDecimal.ZERO); o.setPayAmount(BigDecimal.ZERO); o.setPayStatus("unpaid"); o.setOrderStatus("pending_payment"); o.setDeliveryStatus("pending"); o.setInstallationStatus("none"); o.setCreatedAt(LocalDateTime.now()); o.setUpdatedAt(LocalDateTime.now()); orders.insert(o);
        BigDecimal goods=BigDecimal.ZERO, installFee=BigDecimal.ZERO; List<ClientOrderItem> saved=new ArrayList<>();
        for(Map<String,Object> x:req){ SupplierProduct p=product(L(x.get("product_id"))); int q=Math.max(I(x.get("quantity"),1),1); boolean ins=B(x.get("support_installation")); if(products.deductStock(p.getId(),q)==0) throw new IllegalArgumentException("Insufficient stock: "+p.getName()); BigDecimal a=p.getPrice().multiply(BigDecimal.valueOf(q)); goods=goods.add(a); if(ins) installFee=installFee.add(new BigDecimal("99.00")); ClientOrderItem oi=oi(o,p,q,a,ins); orderItems.insert(oi); saved.add(oi); }
        o.setProductAmount(goods); o.setInstallationFee(installFee); o.setPayAmount(goods.add(installFee)); orders.updateById(o); carts.deleteActiveByUserId(currentUserId()); return orderResult(o,saved);
    }

    @Transactional
    public Map<String,Object> payOrder(Long orderId,Map<String,Object> body){
        ClientOrder o=orders.selectById(orderId); if(o==null) throw new IllegalArgumentException("Cart is empty"); List<ClientOrderItem> its=orderItems.selectByOrderId(orderId); if(!"paid".equals(o.getPayStatus())){ ClientPayment p=new ClientPayment(); p.setPaymentNo(no("PY")); p.setOrderId(o.getId()); p.setOrderNo(o.getOrderNo()); p.setUserId(o.getUserId()); p.setPayChannel(s(body,"pay_channel","mock_pay")); p.setPayAmount(o.getPayAmount()); p.setStatus("success"); p.setPaidAt(LocalDateTime.now()); p.setCreatedAt(LocalDateTime.now()); p.setUpdatedAt(LocalDateTime.now()); payments.insert(p); o.setPayStatus("paid"); o.setOrderStatus("pending_delivery"); o.setPaidAt(LocalDateTime.now()); orders.updateById(o); afterPay(o,its); } return orderResult(o,its);
    }

    public Map<String,Object> orders(Integer page,Integer pageSize,String status){ int p=page(page), s=size(pageSize); List<ClientOrder> list=orders.selectClientOrders(currentUserId(),status,(p-1)*s,s); return m("list",list.stream().map(x->orderResult(x,orderItems.selectByOrderId(x.getId()))).toList(),"total",orders.countClientOrders(currentUserId(),status),"page",p,"page_size",s); }
    public Map<String,Object> orderDetail(Long orderId){ ClientOrder o=orders.selectById(orderId); if(o==null) throw new IllegalArgumentException("Cart is empty"); return m("order",orderResult(o,orderItems.selectByOrderId(orderId)),"delivery",fulfills.selectClientFulfillmentByOrderNo(o.getOrderNo()),"installation",installs.selectClientInstallationByOrderNo(o.getOrderNo())); }
    @Transactional public Map<String,Object> receiveOrder(Long orderId){ ClientOrder o=orders.selectById(orderId); if(o==null) throw new IllegalArgumentException("Cart is empty"); o.setOrderStatus("completed"); o.setDeliveryStatus("received"); o.setReceivedAt(LocalDateTime.now()); o.setCompletedAt(LocalDateTime.now()); orders.updateById(o); return orderResult(o,orderItems.selectByOrderId(orderId)); }
    @Transactional public Map<String,Object> createAftersale(Map<String,Object> body){ ClientOrder o=orders.selectById(L(body.get("order_id"))); if(o==null) throw new IllegalArgumentException("Cart is empty"); ClientOrderItem first=orderItems.selectByOrderId(o.getId()).stream().findFirst().orElseThrow(()->new IllegalArgumentException("???????")); String no=no("AS"); SupplierAftersaleCase sc=new SupplierAftersaleCase(); sc.setSupplierId(o.getSupplierId()); sc.setCaseNo(no); sc.setOrderNo(o.getOrderNo()); sc.setType(s(body,"type","refund")); sc.setDescription(s(body,"description","????")); sc.setStatus("pending"); sc.setRemainingHours(72); sc.setSolution(s(body,"solution",null)); sc.setAttachments(s(body,"photos",null)); sc.setReviewStatus("pending"); sc.setCreatedAt(LocalDateTime.now()); sc.setUpdatedAt(LocalDateTime.now()); scases.insert(sc); ClientAftersale ca=new ClientAftersale(); ca.setCaseNo(no); ca.setSupplierCaseId(sc.getId()); ca.setOrderId(o.getId()); ca.setOrderNo(o.getOrderNo()); ca.setUserId(o.getUserId()); ca.setSupplierId(o.getSupplierId()); ca.setProductId(first.getProductId()); ca.setType(sc.getType()); ca.setDescription(sc.getDescription()); ca.setSolution(sc.getSolution()); ca.setPhotos(sc.getAttachments()); ca.setStatus("pending"); ca.setReviewStatus("pending"); ca.setCreatedAt(LocalDateTime.now()); ca.setUpdatedAt(LocalDateTime.now()); clientAftersales.insert(ca); o.setOrderStatus("aftersale"); orders.updateById(o); return m("case_id",ca.getId(),"case_no",no,"supplier_case_id",sc.getId(),"status","pending"); }
    public Map<String,Object> aftersales(Integer page,Integer pageSize,String status){ ClientAftersale q=qAftersale(status); List<ClientAftersale> list=clientAftersales.selectByCondition(q); return m("list",list,"total",list.size(),"page",page(page),"page_size",size(pageSize)); }
    public Map<String,Object> profileOverview(){ ClientUser u=user(); return m("user",m("user_id",u.getId(),"nickname",u.getNickname(),"mobile",u.getMobile(),"level",u.getLevel(),"points",u.getPoints()),"stats",m("orders",orders.countClientOrders(u.getId(),null),"cart",carts.countActiveByUserId(u.getId()))); }

    private void afterPay(ClientOrder o,List<ClientOrderItem> its){
        for(ClientOrderItem i:its){
            SupplierFulfillment f=new SupplierFulfillment();
            f.setSupplierId(i.getSupplierId()); f.setDeliveryNo(no("DL")); f.setOrderNo(o.getOrderNo());
            f.setArea(extractArea(o.getReceiverAddress())); f.setWarehouse("成都总仓"); f.setCustomerName(o.getReceiverName()); f.setCustomerAddress(o.getReceiverAddress());
            f.setAppointmentDate(LocalDate.now().plusDays(1)); f.setAppointmentTime("09:00-18:00");
            f.setItemSummary(i.getProductName()); f.setQuantity(i.getQuantity()); f.setVolume(BigDecimal.ZERO); f.setWeight(BigDecimal.ZERO);
            f.setStatus("pending"); f.setCreatedAt(LocalDateTime.now()); f.setUpdatedAt(LocalDateTime.now()); fulfills.insert(f);
            wallets.insert(wallet(i.getSupplierId(),o.getOrderNo(),i.getAmount()));
            metrics.upsertOrderMetrics(i.getSupplierId(),i.getProductId(),LocalDate.now(),i.getQuantity(),i.getAmount());
            analytics.upsertOrderAnalytics(i.getSupplierId(),LocalDate.now(),1,i.getAmount());
        }
    }
    private ClientOrderItem oi(ClientOrder o,SupplierProduct p,int q,BigDecimal a,boolean ins){ ClientOrderItem i=new ClientOrderItem(); i.setOrderId(o.getId()); i.setOrderNo(o.getOrderNo()); i.setSupplierId(p.getSupplierId()); i.setProductId(p.getId()); i.setProductName(p.getName()); i.setProductImage(img(p)); i.setCategory(p.getCategory()); i.setMaterial(p.getMaterial()); i.setColor(p.getColor()); i.setSize(p.getSize()); i.setModel(p.getModel()); i.setPrice(p.getPrice()); i.setQuantity(q); i.setAmount(a); i.setSupportInstallation(ins); i.setCreatedAt(LocalDateTime.now()); return i; }
    private SupplierWalletTransaction wallet(Long sid,String orderNo,BigDecimal a){ SupplierWalletTransaction w=new SupplierWalletTransaction(); w.setSupplierId(sid); w.setTitle("????"); w.setDescription(orderNo); w.setAmount(a); w.setDirection("income"); w.setBizType("order"); w.setBizNo(orderNo); w.setCreatedAt(LocalDateTime.now()); return w; }
    private ClientUser user(){ ClientUser q=new ClientUser(); q.setMobile("13800000000"); List<ClientUser> f=users.selectByCondition(q); if(!f.isEmpty()) return f.get(0); q.setUserNo("CU00000001"); q.setNickname("??"); q.setLevel("????"); q.setPoints(0); q.setStatus("active"); q.setCreatedAt(LocalDateTime.now()); q.setUpdatedAt(LocalDateTime.now()); users.insert(q); return q; }
    private SupplierProduct product(Long id){ if(id==null) throw new IllegalArgumentException("??ID????"); SupplierProduct p=products.selectClientProductById(id); if(p==null) throw new IllegalArgumentException("?????????"); return p; }
    private Map<String,Object> pm(SupplierProduct p){ return m("id",p.getId(),"name",p.getName(),"image",img(p),"category",p.getCategory(),"price",p.getPrice(),"stock",p.getStock(),"support_installation",p.getSupportInstallation()); }
    private Map<String,Object> orderResult(ClientOrder o,List<ClientOrderItem> its){ return m("order_id",o.getId(),"order_no",o.getOrderNo(),"status",o.getOrderStatus(),"pay_status",o.getPayStatus(),"pay_amount",o.getPayAmount(),"product_summary",orderSummary(its),"receiver_address",o.getReceiverAddress(),"receiver_name",o.getReceiverName(),"receiver_mobile",o.getReceiverMobile(),"items",its.stream().map(this::orderItemMap).toList()); }
    private Map<String,Object> orderItemMap(ClientOrderItem i){ return m("order_item_id",i.getId(),"product_id",i.getProductId(),"supplier_id",i.getSupplierId(),"product_name",i.getProductName(),"product_image",i.getProductImage(),"price",i.getPrice(),"quantity",i.getQuantity(),"amount",i.getAmount(),"support_installation",i.getSupportInstallation()); }
    private String orderSummary(List<ClientOrderItem> its){ return its.stream().map(i -> i.getProductName()+" x "+i.getQuantity()).reduce((a,b) -> a+", "+b).orElse(""); }
    private ClientAftersale qAftersale(String status){ ClientAftersale q=new ClientAftersale(); q.setUserId(currentUserId()); if(status!=null&&!"all".equals(status)) q.setStatus(status); return q; }
    private String img(SupplierProduct p){ String s=p.getImages(); if(s==null||s.isBlank()) return IMG; if(s.startsWith("[")&&s.contains("\"")){ int a=s.indexOf('"')+1, b=s.indexOf('"',a); if(a>0&&b>a) return s.substring(a,b); } return s; }
    private String no(String p){ return p+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")); }
    private Long L(Object v){ return v==null?null:(v instanceof Number n?n.longValue():Long.parseLong(String.valueOf(v))); }
    private int I(Object v,int d){ return v==null?d:(v instanceof Number n?n.intValue():Integer.parseInt(String.valueOf(v))); }
    private boolean B(Object v){ return v instanceof Boolean b?b:v!=null&&("true".equalsIgnoreCase(String.valueOf(v))||"1".equals(String.valueOf(v))); }
    private String s(Map<String,Object> m,String k,String d){ if(m==null||m.get(k)==null) return d; String v=String.valueOf(m.get(k)).trim(); return v.isEmpty()?d:v; }
    private int page(Integer p){ return p==null||p<1?1:p; }
    private int size(Integer s){ return s==null||s<1?10:s; }
    private Map<String,Object> m(Object...a){ Map<String,Object> r=new LinkedHashMap<>(); for(int i=0;i<a.length;i+=2) r.put(String.valueOf(a[i]), a[i+1]); return r; }
}
