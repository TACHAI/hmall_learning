package com.hmall.dao;

        import com.hmall.pojo.Cart;
        import org.apache.ibatis.annotations.Param;

        import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    Cart selectCartByUserIdProduct(@Param("userId") Integer userId,@Param("productId") Integer productId);
    List<Cart> selectCartByUserId(Integer userId);
    int selectCartProductCheckedStatusByUserId(Integer userId);
    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productList") List<String> productIdList);
    int checkOrUnchekedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);
    int selectCartProductCount(@Param("userId") Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}