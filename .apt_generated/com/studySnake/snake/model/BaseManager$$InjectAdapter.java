// Code generated by dagger-compiler.  Do not edit.
package com.studySnake.snake.model;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<BaseManager>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code BaseManager} and its
 * dependencies.
 * 
 * Being a {@code Provider<BaseManager>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<BaseManager>} and handling injection
 * of annotated fields.
 */
public final class BaseManager$$InjectAdapter extends Binding<BaseManager>
    implements Provider<BaseManager>, MembersInjector<BaseManager> {
  private Binding<com.squareup.otto.Bus> bus;

  public BaseManager$$InjectAdapter() {
    super("com.studySnake.snake.model.BaseManager", "members/com.studySnake.snake.model.BaseManager", NOT_SINGLETON, BaseManager.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", BaseManager.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(bus);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<BaseManager>}.
   */
  @Override
  public BaseManager get() {
    BaseManager result = new BaseManager();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<BaseManager>}.
   */
  @Override
  public void injectMembers(BaseManager object) {
    object.bus = bus.get();
  }
}