// Code generated by dagger-compiler.  Do not edit.
package com.studySnake.snake;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<BaseFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code BaseFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<BaseFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<BaseFragment>} and handling injection
 * of annotated fields.
 */
public final class BaseFragment$$InjectAdapter extends Binding<BaseFragment>
    implements Provider<BaseFragment>, MembersInjector<BaseFragment> {
  private Binding<com.squareup.otto.Bus> bus;

  public BaseFragment$$InjectAdapter() {
    super("com.studySnake.snake.BaseFragment", "members/com.studySnake.snake.BaseFragment", NOT_SINGLETON, BaseFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", BaseFragment.class, getClass().getClassLoader());
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
   * {@code Provider<BaseFragment>}.
   */
  @Override
  public BaseFragment get() {
    BaseFragment result = new BaseFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<BaseFragment>}.
   */
  @Override
  public void injectMembers(BaseFragment object) {
    object.bus = bus.get();
  }
}
