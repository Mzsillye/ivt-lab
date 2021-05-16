package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.util.Arrays;

public class GT4500Test {


  private GT4500 ship;
  private TorpedoStore primary;
  private TorpedoStore secondary;

  @BeforeEach
  public void init() {
    primary = mock(TorpedoStore.class);
    when(primary.fire(1)).thenReturn(true);
    secondary = mock(TorpedoStore.class);
    when(secondary.fire(1)).thenReturn(true);
    this.ship = new GT4500(primary, secondary);
  }

  @Test
  public void fireTorpedo_Single_Success() {
    // Arrange

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(primary.isEmpty()).thenReturn(false);
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(false);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_StartsWithFirst() {
    //Arange

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verifyNoMoreInteractions(secondary);
  }

  @Test
  public void fireTorpedo_Single_AlternatesStores()
  {
    //Arange

    //Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, result1);
    assertEquals(true, result2);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_EmptyStore()
  {
    //Arange
    when(primary.isEmpty()).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, result);
    verify(primary,times(0)).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_OnlyTriesOneWhenError()
  {
    //Arange
    when(primary.fire(1)).thenReturn(false);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(false, result);
    verify(primary,times(1)).fire(1);
    verify(secondary,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_SuccessWhenOneIsSuccess()
  {
    //Arange
    when(primary.fire(1)).thenReturn(false);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    //Assert
    assertEquals(true, result);
    verify(primary,times(1)).fire(1);
    verify(secondary,times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_EmptyStores()
  {
    //Arange
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    //Assert
    assertEquals(false, result);
    verify(primary,times(0)).fire(1);
    verify(secondary,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_EmptySecondary()
  {
    //Arange
    when(secondary.isEmpty()).thenReturn(true);

    //Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    verify(primary,times(1)).fire(1);
    verify(secondary,times(0)).fire(1);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, result1);
    assertEquals(true, result2);
    verify(primary,times(2)).fire(1);
    verify(secondary,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_NonExistentMode()
  {
    //Arange

    //Act
    boolean result = ship.fireTorpedo(Enum.valueOf(FiringMode.class,"SOMETHING"));

    //Assert
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_OneBulletInPrimary()
  {
    //Arange
    when(secondary.isEmpty()).thenReturn(true);

    //Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    when(primary.isEmpty()).thenReturn(true);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    assertEquals(true, result1);
    assertEquals(false, result2);
  }
  @Test
  public void fireTorpedo_Single_EmptyStores()
  {
    //Arange
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_StoresFail()
  {
    //Arange
    when(primary.isEmpty()).thenReturn(false);
    when(primary.fire(1)).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(false);
    when(secondary.fire(1)).thenReturn(false);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    //Assert
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_SecondaryFails()
  {
    //Arange
    when(secondary.isEmpty()).thenReturn(false);
    when(secondary.fire(1)).thenReturn(false);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    //Assert
    assertEquals(true, result);
  }
}
